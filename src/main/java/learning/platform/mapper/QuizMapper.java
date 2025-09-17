package learning.platform.mapper;

import learning.platform.dto.*;
import learning.platform.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Interfaz Mapper para la conversión de entidades relacionadas con quizzes a DTOs y viceversa.
 * Utiliza el framework MapStruct para generar automáticamente la implementación en tiempo de compilación.
 * Esto ayuda a mantener el código de conversión limpio y libre de errores manuales.
 */
@Mapper(componentModel = "spring")
public interface QuizMapper {

    /**
     * Instancia singleton del mapper para uso en componentes que no son de Spring.
     * Aunque se prefiere la inyección de dependencias en un entorno Spring.
     */
    QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    /**
     * Convierte una entidad {@link Quiz} a un DTO {@link QuizResponse}.
     * Mapea los IDs de las entidades relacionadas (Course, Lesson) directamente
     * a los campos del DTO y mapea la lista de preguntas.
     * @param quiz La entidad Quiz a convertir.
     * @return El DTO QuizResponse resultante.
     */
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "lessonId", source = "lesson.id")
    @Mapping(target = "questions", source = "questions")
    QuizResponse toQuizResponse(Quiz quiz);

    /**
     * Convierte una entidad {@link Question} a un DTO {@link QuestionResponse}.
     * @param question La entidad Question a convertir.
     * @return El DTO QuestionResponse resultante.
     */
    @Mapping(target = "options", source = "options")
    QuestionResponse toQuestionResponse(Question question);

    /**
     * Convierte una entidad {@link AnswerOption} a un DTO {@link AnswerOptionDTO}.
     * @param answerOption La entidad AnswerOption a convertir.
     * @return El DTO AnswerOptionDTO resultante.
     */
    AnswerOptionDTO toAnswerOptionDTO(AnswerOption answerOption);

    /**
     * Convierte una solicitud de creación de quiz (DTO) a una entidad {@link Quiz}.
     * El campo de preguntas se ignora en este mapeo para ser gestionado manualmente.
     * @param request La solicitud de creación QuizCreateRequest.
     * @return La entidad Quiz resultante.
     */
    @Mapping(target = "questions", ignore = true)
    Quiz toQuiz(QuizCreateRequest request);

    /**
     * Convierte una solicitud de creación de pregunta (DTO) a una entidad {@link Question}.
     * Las opciones se ignoran en este mapeo para ser gestionadas manualmente.
     * @param request La solicitud de creación QuestionCreateRequest.
     * @return La entidad Question resultante.
     */
    @Mapping(target = "options", ignore = true)
    Question toQuestion(QuestionCreateRequest request);

    /**
     * Convierte un DTO de opción de respuesta a una entidad {@link AnswerOption}.
     * @param dto El DTO AnswerOptionDTO a convertir.
     * @return La entidad AnswerOption resultante.
     */
    AnswerOption toAnswerOption(AnswerOptionDTO dto);

    /**
     * Convierte una lista de entidades {@link Quiz} a una lista de DTOs {@link QuizResponse}.
     * @param quizzes La lista de entidades Quiz.
     * @return Una lista de DTOs QuizResponse.
     */
    List<QuizResponse> toQuizResponseList(List<Quiz> quizzes);

    /**
     * Convierte una lista de entidades {@link Question} a una lista de DTOs {@link QuestionResponse}.
     * @param questions La lista de entidades Question.
     * @return Una lista de DTOs QuestionResponse.
     */
    List<QuestionResponse> toQuestionResponseList(List<Question> questions);

    /**
     * Convierte una lista de entidades {@link AnswerOption} a una lista de DTOs {@link AnswerOptionDTO}.
     * @param answerOptions La lista de entidades AnswerOption.
     * @return Una lista de DTOs AnswerOptionDTO.
     */
    List<AnswerOptionDTO> toAnswerOptionDtoList(List<AnswerOption> answerOptions);
}