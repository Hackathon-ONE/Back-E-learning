package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Progreso completo de un estudiante en un curso")
public record ProgressCourseResponse(
        @Schema(description = "ID del curso") Long courseId,
        @Schema(description = "ID del estudiante") Long studentId,
        @Schema(description = "Porcentaje de progreso") Integer progressPercent,
        @Schema(description = "Lecciones completadas") List<LessonSummary> lessonsCompleted,
        @Schema(description = "Quizzes completados") List<QuizSummary> quizzesCompleted
) {
    public record LessonSummary(
            @Schema(description = "ID de la lección") Long id,
            @Schema(description = "Título de la lección") String title
    ) {}

    public record QuizSummary(
            @Schema(description = "ID del quiz") Long id,
            @Schema(description = "Puntaje obtenido") Integer score
    ) {}
}
