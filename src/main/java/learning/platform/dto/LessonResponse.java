package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para datos de una lección")
public record LessonResponse(
        @Schema(description = "ID único de la lección", example = "1")
        Long id,

        @Schema(description = "ID del curso al que pertenece la lección", example = "1")
        Long courseId,

        @Schema(description = "Título de la lección", example = "Introducción a Java")
        String title,

        @Schema(description = "Índice de orden de la lección dentro del curso", example = "1")
        Integer orderIndex,

        @Schema(description = "Duración de la lección en minutos.", example = "30")
        Integer durationMinutes // 👈 minutos en la respuesta

) {}