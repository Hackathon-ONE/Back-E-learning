package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AnswerOptionDTO", description = "DTO para devolver una opción de respuesta en el frontend")
public class AnswerOptionDTO {

    @Schema(description = "ID de la opción de respuesta", example = "1234")
    private Long id;

    @Schema(description = "Texto de la opción", example = "París")
    private String text;

    public AnswerOptionDTO() {}

    public AnswerOptionDTO(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
