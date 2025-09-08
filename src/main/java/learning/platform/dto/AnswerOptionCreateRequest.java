package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "AnswerOptionCreateRequest", description = "DTO para crear una opción de respuesta")
public class AnswerOptionCreateRequest {

    @NotBlank(message = "El texto de la opción no puede estar vacío")
    @Size(min = 2, max = 1000, message = "El texto de la opción debe tener entre 2 y 1000 caracteres")
    @Schema(
            description = "Texto de la opción",
            example = "París",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String text;

    @NotNull(message = "Debe especificarse si la opción es correcta o no")
    @Schema(
            description = "Marca si esta opción es la correcta (solo usada en servidor al crear)",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Boolean correct;

    public AnswerOptionCreateRequest() {
    }

    public AnswerOptionCreateRequest(String text, Boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    // Getters y setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
