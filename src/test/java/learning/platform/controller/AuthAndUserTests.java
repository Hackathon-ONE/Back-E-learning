package learning.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.platform.config.TestSecurityConfig;
import learning.platform.dto.UserLoginRequest;
import learning.platform.dto.UserRegisterRequest;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import learning.platform.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
class AuthAndUserTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String STUDENT_EMAIL = "teststudent@example.com";
    private static final String ADMIN_EMAIL = "testadmin@example.com";
    private static final String PASSWORD = "SecurePass123!";
    private String studentToken;
    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        // Preparar usuarios de prueba para que los filtros de seguridad funcionen
        User studentUser = new User();
        studentUser.setEmail(STUDENT_EMAIL);
        studentUser.setPasswordHash(passwordEncoder.encode(PASSWORD));
        studentUser.setRole(Role.STUDENT);
        userRepository.save(studentUser);

        User adminUser = new User();
        adminUser.setEmail(ADMIN_EMAIL);
        adminUser.setPasswordHash(passwordEncoder.encode(PASSWORD));
        adminUser.setRole(Role.ADMIN);
        userRepository.save(adminUser);

        // Obtener un token JWT real para el estudiante
        UserLoginRequest studentLoginRequest = new UserLoginRequest(STUDENT_EMAIL, PASSWORD);
        MvcResult studentLoginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentLoginRequest)))
                .andReturn();
        studentToken = objectMapper.readTree(studentLoginResult.getResponse().getContentAsString()).get("token").asText();

        // Obtener un token JWT real para el administrador
        UserLoginRequest adminLoginRequest = new UserLoginRequest(ADMIN_EMAIL, PASSWORD);
        MvcResult adminLoginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andReturn();
        adminToken = objectMapper.readTree(adminLoginResult.getResponse().getContentAsString()).get("token").asText();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        UserRegisterRequest registerRequest = new UserRegisterRequest(
                "New User",
                "newuser@example.com",
                "SecurePass456!",
                Role.STUDENT.name(),
                "",
                ""
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnUserByEmailWithAdminRole() throws Exception {
        // Se usa el token real obtenido en el método setUp()
        mockMvc.perform(get("/api/users/email/" + ADMIN_EMAIL)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnForbiddenWhenStudentRoleTriesToAccessAdminEndpoint() throws Exception {
        // Se usa el token real obtenido en el método setUp()
        mockMvc.perform(get("/api/users/email/" + STUDENT_EMAIL)
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenNoTokenIsProvided() throws Exception {
        // No se pasa el token de autenticación
        mockMvc.perform(get("/api/users/email/" + ADMIN_EMAIL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnConflictWhenRegisteringUserWithExistingEmail() throws Exception {
        UserRegisterRequest registerRequest = new UserRegisterRequest(
                "Existing User",
                STUDENT_EMAIL, // Usamos un email que ya existe
                "SecurePass789!",
                Role.STUDENT.name(),
                "",
                ""
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldAllowAdminToFindAUser() throws Exception {
        // Se usa el token real del administrador
        mockMvc.perform(get("/api/users/email/" + STUDENT_EMAIL)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @AfterEach
    void tearDown() {
        // Limpiar la base de datos después de cada test
        userRepository.deleteAll();
    }
}
