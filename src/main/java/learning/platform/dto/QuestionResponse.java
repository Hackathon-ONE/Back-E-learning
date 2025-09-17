package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "QuestionResponse", description = "DTO con pregunta y sus opciones para mostrar en la UI")
public class QuestionResponse {

    @Schema(description = "ID de la pregunta", example = "987", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "Texto de la pregunta", example = "¿Cuál es la capital de Francia?", requiredMode = Schema.RequiredMode.REQUIRED)
    private String text;

    @Schema(description = "Opciones disponibles para responder", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<AnswerOptionDTO> options;

    @Schema(description = "Puntos que vale la pregunta", example = "10")
    private Integer points;

    @Schema(description = "Tiempo límite en segundos (si aplica)", example = "60")
    private Integer timeLimitSeconds;

    public QuestionResponse() {}

    public QuestionResponse(Long id, String text, List<AnswerOptionDTO> options, Integer points, Integer timeLimitSeconds) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.points = points;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<AnswerOptionDTO> getOptions() { return options; }
    public void setOptions(List<AnswerOptionDTO> options) { this.options = options; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Integer getTimeLimitSeconds() { return timeLimitSeconds; }
    public void setTimeLimitSeconds(Integer timeLimitSeconds) { this.timeLimitSeconds = timeLimitSeconds; }
}
