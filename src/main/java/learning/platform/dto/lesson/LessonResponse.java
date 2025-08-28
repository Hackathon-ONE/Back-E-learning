package learning.platform.dto.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import learning.platform.enums.ContentType;
import java.time.Duration;

@Schema(description = "DTO de respuesta para datos de una lección")
public record LessonResponse(
        @Schema(description = "ID único de la lección", example = "1")
        Long id,

        @Schema(description = "ID del curso al que pertenece la lección", example = "1")
        Long courseId,

        @Schema(description = "Título de la lección", example = "Introducción a Java")
        String title,

        @Schema(description = "URL del contenido de la lección", example = "https://example.com/video.mp4")
        String contentUrl,

        @Schema(description = "Tipo de contenido de la lección", example = "VIDEO")
        ContentType contentType,

        @Schema(description = "Índice de orden de la lección dentro del curso", example = "1")
        Integer orderIndex,

        @Schema(description = "Duración de la lección (opcional)", example = "PT30M")
        Duration duration
) {}