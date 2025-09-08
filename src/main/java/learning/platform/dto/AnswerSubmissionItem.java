package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AnswerSubmissionItem", description = "Respuesta para una pregunta concreta")
public class AnswerSubmissionItem {

    @NotNull
    @Schema(description = "ID de la pregunta", example = "987")
    private Long questionId;

    @Schema(description = "ID de la opción seleccionada (para preguntas de opción múltiple)", example = "456")
    private Long selectedOptionId;

    @Schema(description = "Texto respuesta libre (si aplica)", example = "Mi respuesta libre")
    private String answerText;

    public AnswerSubmissionItem() {}

    public AnswerSubmissionItem(Long questionId, Long selectedOptionId, String answerText) {
        this.questionId = questionId;
        this.selectedOptionId = selectedOptionId;
        this.answerText = answerText;
    }

    // getters / setters
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public Long getSelectedOptionId() { return selectedOptionId; }
    public void setSelectedOptionId(Long selectedOptionId) { this.selectedOptionId = selectedOptionId; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}