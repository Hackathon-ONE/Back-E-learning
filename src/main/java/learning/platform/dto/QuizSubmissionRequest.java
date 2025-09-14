package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(
        name = "Solicitud de Envío de Quiz",
        description = "DTO para enviar las respuestas del quizz."
)
public class QuizSubmissionRequest {

    @NotEmpty(message = "Debe enviar al menos una respuesta")
    @NotNull(message = "La lista de respuestas no puede ser nula")
    @Schema(description = "Lista de respuestas del estudiante")
    private List<AnswerSubmissionItem> answers;

    public QuizSubmissionRequest() {}

    public QuizSubmissionRequest(List<AnswerSubmissionItem> answers) {
        this.answers = answers;
    }

    // --- Getters y Setters ---

    public List<AnswerSubmissionItem> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerSubmissionItem> answers) {
        this.answers = answers;
    }
}