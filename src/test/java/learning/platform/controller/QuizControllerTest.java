package learning.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.platform.dto.AnswerSubmissionRequest;
import learning.platform.dto.QuizCreateRequest;
import learning.platform.dto.QuizSubmissionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateQuizSuccessfully() throws Exception {
        QuizCreateRequest request = new QuizCreateRequest();
        request.setTitle("Quiz de prueba");
        request.setDescription("Descripción de prueba");
        request.setCourseId(1L);

        mockMvc.perform(post("/api/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Quiz de prueba"))
                .andExpect(jsonPath("$.courseId").value(1));
    }

    @Test
    void shouldGetQuizById() throws Exception {
        mockMvc.perform(get("/api/quizzes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldSubmitQuizAnswers() throws Exception {
        QuizSubmissionRequest request = new QuizSubmissionRequest();
        request.setEnrollmentId(10L);

        AnswerSubmissionRequest answer = new AnswerSubmissionRequest();
        answer.setQuestionId(987L);
        answer.setSelectedOptionId(1234L);

        request.setAnswers(Collections.singletonList(answer));

        mockMvc.perform(post("/api/quizzes/1/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quizId").value(1))
                .andExpect(jsonPath("$.enrollmentId").value(10));
    }
}

