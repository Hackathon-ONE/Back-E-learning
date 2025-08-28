package learning.platform.dto.progress;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

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
        Instant updatedAt
) {}