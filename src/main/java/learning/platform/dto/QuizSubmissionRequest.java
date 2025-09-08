package learning.platform.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class QuizSubmissionRequest {

    @NotNull(message = "El enrollmentId es obligatorio")
    private Long enrollmentId;

    @NotEmpty(message = "Debe enviar al menos una respuesta")
    private List<AnswerSubmissionRequest> answers;

    public QuizSubmissionRequest() {}

    public QuizSubmissionRequest(Long enrollmentId, List<AnswerSubmissionRequest> answers) {
        this.enrollmentId = enrollmentId;
        this.answers = answers;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public List<AnswerSubmissionRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerSubmissionRequest> answers) {
        this.answers = answers;
    }
}
