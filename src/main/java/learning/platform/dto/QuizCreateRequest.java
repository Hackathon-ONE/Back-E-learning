package learning.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class QuizCreateRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 255, message = "El título debe tener entre 3 y 255 caracteres")
    private String title;

    private String description;

    @NotNull(message = "Debe asociar el quiz a un curso o lección")
    private Long courseId;

    private Long lessonId; // opcional, si el quiz está en una lección

    @NotNull(message = "Debe incluir al menos una pregunta")
    @Size(min = 1, message = "Debe incluir al menos una pregunta")
    private List<QuestionCreateRequest> questions;

    // --- Getters y Setters ---
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public List<QuestionCreateRequest> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionCreateRequest> questions) {
        this.questions = questions;
    }
}
