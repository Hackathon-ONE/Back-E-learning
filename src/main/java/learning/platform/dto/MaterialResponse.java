package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import learning.platform.enums.ContentType;

@Schema(description = "DTO de respuesta para datos de un material.")
public record MaterialResponse(
        @Schema(description = "ID único del material", example = "1")
        Long id,

        @Schema(description = "ID de la lección a la que pertenece el material", example = "10")
        Long lessonId,

        @Schema(description = "Título del material", example = "Video introductorio")
        String title,

        @Schema(description = "URL del contenido del material", example = "https://example.com/video.mp4")
        String contentUrl,

        @Schema(description = "Tipo de contenido del material", example = "VIDEO")
        ContentType contentType
) {
}
