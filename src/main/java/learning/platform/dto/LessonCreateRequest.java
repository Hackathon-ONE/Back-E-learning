package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "DTO para la creación de una lección")
public record LessonCreateRequest(

        @Schema(description = "Título de la lección", example = "Introducción a Java")
        @NotBlank(message = "El título no puede estar vacío")
        String title,

        @Schema(description = "Índice de orden de la lección dentro del curso", example = "1")
        @PositiveOrZero(message = "El índice de orden debe ser cero o positivo")
        Integer orderIndex,

        @NotNull(message = "La duración es obligatoria")
        @Min(1)
        @Schema(description = "Duración de la lección. En minutos.", example = "30")
        Integer durationMinutes // 👈 minutos en el request.
) {
    public Long durationSeconds() {
        return durationMinutes != null ? durationMinutes * 60L : null;
    }
}