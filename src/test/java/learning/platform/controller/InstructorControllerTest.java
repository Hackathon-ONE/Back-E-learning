package learning.platform.controller;

import learning.platform.service.InstructorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(InstructorControllerTest.Config.class)
class InstructorControllerTest {

    @TestConfiguration
    static class Config {
        @Mock
        private InstructorService instructorService;

        @Bean
        public InstructorController instructorController() {
            return new InstructorController(instructorService);
        }
    }

    @InjectMocks
    private InstructorController instructorController;

    @Mock
    private InstructorService instructorService;

    @Test
    void testGetStudentsByInstructor() {
        // dado
        Long instructorId = 1L;
        when(instructorService.getStudentsByInstructor(instructorId))
                .thenReturn(List.of());

        // cuando
        var response = instructorController.getStudents(instructorId);

        // entonces
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
