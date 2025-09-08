package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "AnswerSubmissionRequest",
        description = "DTO con la respuesta seleccionada por el estudiante"
)
public class AnswerSubmissionRequest {

    @NotNull(message = "El ID de la pregunta es obligatorio")
    @Schema(
            description = "ID de la pregunta",
            example = "987",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long questionId;

    @NotNull(message = "Debe seleccionar una opción de respuesta")
    @Schema(
            description = "ID de la opción seleccionada",
            example = "1234",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long selectedOptionId;

    public AnswerSubmissionRequest() {}

    public AnswerSubmissionRequest(Long questionId, Long selectedOptionId) {
        this.questionId = questionId;
        this.selectedOptionId = selectedOptionId;
    }

    // Getters y Setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(Long selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }
}
