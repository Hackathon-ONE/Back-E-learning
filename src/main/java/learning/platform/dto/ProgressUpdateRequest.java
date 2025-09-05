package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.Instant;

public record ProgressUpdateRequest(
        @Schema(description = "ID de la inscripción asociada.", example = "1")
        @NotNull(message = "El ID de la inscripción es obligatorio")
        Long enrollmentId,
        @Schema(description = "ID de la lección asociada.", example = "1")
        @NotNull(message = "El ID de la lección es obligatorio")
        Long lessonId,
        @Schema(description = "Indica si la lección está completada.", example = "true")
        @NotNull(message = "El estado de completado es obligatorio")
        Boolean completed,
        @Schema(description = "Puntaje obtenido (opcional).", example = "85")
        @PositiveOrZero(message = "El puntaje debe ser cero o positivo")
        Integer score,
        @Schema(description = "Porcentaje de completitud de la lección. Entre 0.00 y 100.00")
        @DecimalMin("0.00")
        @Digits(integer = 5, fraction = 2)
        BigDecimal completionPercentage,
        @Schema(description = "Fecha y hora de la última actualización.")
        Instant updatedAt
) {}