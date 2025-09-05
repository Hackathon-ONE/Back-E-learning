package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "DTO de respuesta para el progreso de una lección dentro de una inscripción")
public record ProgressResponse(
        @Schema(description = "ID único del progreso.", example = "1")
        Long id,

        @Schema(description = "ID de la inscripción asociada.", example = "1")
        Long enrollmentId,

        @Schema(description = "ID de la lección asociada.", example = "1")
        Long lessonId,

        @Schema(description = "Indica si la lección está completada.", example = "true")
        Boolean completed,

        @Schema(description = "Puntaje obtenido (opcional).", example = "85")
        Integer score,

        @Schema(description = "Fecha y hora de la última actualización.")
        Instant updatedAt,

        @Schema(description = "Porcentaje de completitud de la lección.", example = "75.0")
        BigDecimal completionPercentage
) {}
