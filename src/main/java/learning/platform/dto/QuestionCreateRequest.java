package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "QuestionCreateRequest", description = "DTO para crear/editar una pregunta con sus opciones")
public class QuestionCreateRequest {

    @NotBlank
    @Size(max = 2000)
    @Schema(description = "Texto de la pregunta", example = "¿Cuál es la capital de Francia?")
    private String text;

    @NotNull
    @Size(min = 2, message = "La pregunta debe tener al menos 2 opciones")
    @Schema(description = "Opciones posibles para la pregunta")
    private List<AnswerOptionCreateRequest> options;

    @NotNull(message = "Debe indicar cuál opción es la correcta")
    @Schema(description = "Índice de la opción correcta dentro de la lista de opciones", example = "1")
    private Integer correctOptionIndex;

    @Schema(description = "Puntos que vale la pregunta", example = "10")
    private Integer points;

    @Schema(description = "Tiempo límite en segundos para responder (opcional)", example = "60")
    private Integer timeLimitSeconds;

    public QuestionCreateRequest() {}

    public QuestionCreateRequest(String text, List<AnswerOptionCreateRequest> options,
                                 Integer correctOptionIndex, Integer points, Integer timeLimitSeconds) {
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.points = points;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    // getters y setters
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public @NotNull @Size(min = 2, message = "La pregunta debe tener al menos 2 opciones") List<AnswerOptionCreateRequest> getOptions() { return options; }
    public void setOptions(List<AnswerOptionCreateRequest> options) { this.options = options; }

    public Integer getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(Integer correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Integer getTimeLimitSeconds() { return timeLimitSeconds; }
    public void setTimeLimitSeconds(Integer timeLimitSeconds) { this.timeLimitSeconds = timeLimitSeconds; }
}
