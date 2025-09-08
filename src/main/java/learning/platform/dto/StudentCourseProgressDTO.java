package learning.platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(description = "Información del progreso de un estudiante en sus cursos")
public class StudentCourseProgressDTO {
    @NotNull
    @Schema(description = "ID del estudiante", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nombre completo del estudiante", example = "Juan Pérez")
    private String fullName;

    @Email
    @Schema(description = "Correo electrónico del estudiante", example = "juan@example.com")
    private String email;

    @NotNull
    @Schema(description = "Lista de cursos con progreso")
    private List<CourseProgressDTO> coursesEnrolled;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<CourseProgressDTO> getCoursesEnrolled() { return coursesEnrolled; }
    public void setCoursesEnrolled(List<CourseProgressDTO> coursesEnrolled) { this.coursesEnrolled = coursesEnrolled; }

    // DTO anidado para curso
    @Schema(description = "Progreso de un curso específico")
    public static class CourseProgressDTO {
        @NotNull
        @Schema(description = "ID del curso", example = "10")
        private Long id;
        @NotBlank
        @Schema(description = "Título del curso", example = "Introducción a Java")
        private String title;
        @Schema(description = "Porcentaje de avance", example = "75")
        private int progressPercent;

        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public int getProgressPercent() { return progressPercent; }
        public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }
    }
}