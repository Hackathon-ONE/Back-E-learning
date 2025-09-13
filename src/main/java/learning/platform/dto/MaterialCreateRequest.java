package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import learning.platform.enums.ContentType;

@Schema(description = "DTO para la creación de un material dentro de una lección.")
public record MaterialCreateRequest(

        @Schema(description = "Título del material", example = "Video introductorio")
        @NotBlank(message = "El título no puede estar vacío")
        String title,

        @Schema(description = "URL del contenido del material", example = "https://example.com/video.mp4")
        @NotBlank(message = "La URL no puede estar vacía")
        String contentUrl,

        @Schema(description = "Tipo de contenido del material", example = "VIDEO")
        @NotNull(message = "El tipo de contenido es obligatorio")
        ContentType contentType
) {
}
