package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Respuesta con los datos de un Quiz")
public class QuizResponse {

    @Schema(description = "ID único del quiz", example = "101")
    private Long id;

    @NotNull(message = "El título no puede ser nulo")
    @Size(min = 3, max = 255, message = "El título debe tener entre 3 y 255 caracteres")
    @Schema(description = "Título del quiz", example = "Quiz de Introducción a Java")
    private String title;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Schema(description = "Descripción del quiz", example = "Este quiz evalúa los fundamentos de Java.")
    private String description;

    @NotNull(message = "El cursoId no puede ser nulo")
    @Schema(description = "ID del curso asociado", example = "10")
    private Long courseId;

    @Schema(description = "Preguntas del quiz")
    private List<QuestionResponse> questions;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public List<QuestionResponse> getQuestions() { return questions; }
    public void setQuestions(List<QuestionResponse> questions) { this.questions = questions; }
}