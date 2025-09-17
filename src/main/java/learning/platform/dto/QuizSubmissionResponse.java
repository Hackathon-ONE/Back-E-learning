package learning.platform.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class QuizSubmissionResponse {

    @NotNull(message = "El quizId no puede ser nulo")
    private Long quizId;

    @NotNull(message = "El enrollmentId no puede ser nulo")
    private Long enrollmentId;

    @NotNull(message = "El puntaje no puede ser nulo")
    @Min(value = 0, message = "El puntaje mínimo es 0")
    private Integer score;   // puntaje obtenido

    @NotNull(message = "El campo passed no puede ser nulo")
    private Boolean passed;  // aprobado o no

    @NotNull(message = "El porcentaje no puede ser nulo")
    @Min(value = 0, message = "El porcentaje mínimo es 0")
    @Max(value = 100, message = "El porcentaje máximo es 100")
    private Double percentage; // % de aciertos

    // --- Getters y Setters ---
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
