package learning.platform.service.impl;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import learning.platform.mapper.UserMapper;
import learning.platform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRegisterRequest request = new UserRegisterRequest(
                "Ángeles Escudero",
                "angeles@example.com",
                "SecurePass123!",
                "ADMIN"
        );

        User userEntity = new User();
        userEntity.setId(1L);
        userEntity.setFullName("Ángeles Escudero");
        userEntity.setEmail("angeles@example.com");
        userEntity.setRole(Role.ADMIN);
        userEntity.setPasswordHash("encodedPass123");
        userEntity.setActive(true);

        UserResponse expectedResponse = new UserResponse(
                1L,
                "Ángeles Escudero",
                "angeles@example.com",
                "ADMIN",
                true
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(userEntity);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPass123");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toResponse(userEntity)).thenReturn(expectedResponse);

        UserResponse result = userService.register(request);

        assertNotNull(result);
        assertEquals(expectedResponse.email(), result.email());
        assertEquals(expectedResponse.fullName(), result.fullName());
        assertEquals(expectedResponse.role(), result.role());
        assertEquals(expectedResponse.active(), result.active());
        verify(userRepository).save(userEntity);
    }

    @Test
    void shouldReturnUserById() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Ángeles Escudero");
        user.setEmail("angeles@example.com");
        user.setRole(Role.ADMIN);
        user.setActive(true);

        UserResponse expectedResponse = new UserResponse(
                1L,
                "Ángeles Escudero",
                "angeles@example.com",
                "ADMIN",
                true
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        UserResponse result = userService.getById(1L);

        assertEquals(expectedResponse, result);
    }

    @Test
    void shouldReturnUserByEmail() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Ángeles Escudero");
        user.setEmail("angeles@example.com");
        user.setRole(Role.ADMIN);
        user.setActive(true);

        UserResponse expectedResponse = new UserResponse(
                1L,
                "Ángeles Escudero",
                "angeles@example.com",
                "ADMIN",
                true
        );

        when(userRepository.findByEmail("angeles@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        Optional<UserResponse> result = userService.findByEmail("angeles@example.com");

        assertTrue(result.isPresent());
        assertEquals(expectedResponse, result.get());
    }
}