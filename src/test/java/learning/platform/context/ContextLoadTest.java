package learning.platform.context;

import learning.platform.Application;
import learning.platform.config.TestSecurityConfig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {Application.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContextLoadTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        System.out.println("Perfil activo: test");
        assertNotNull(mockMvc);
    }
}