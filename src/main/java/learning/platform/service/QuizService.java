package learning.platform.service;

import learning.platform.dto.*;
import learning.platform.entity.User;

import java.util.List;

/**
 * Servicio para gestionar Quizzes. Aquí se define todo lo que se puede hacer
 * con los cuestionarios, como crearlos, obtenerlos, actualizarlos y calificarlos.
 */
public interface QuizService {

    /**
     * Crea un nuevo quiz con sus preguntas.
     * @param request El objeto con la información del quiz que se quiere crear.
     * @return El quiz que se acaba de crear, con su información.
     */
    QuizResponse createQuiz(QuizCreateRequest request);

    /**
     * Busca y obtiene los detalles de un quiz usando su ID.
     * @param quizId El número de identificación del quiz que se busca.
     * @return El quiz que se encontró.
     */
    QuizResponse getQuizById(Long quizId);

    /**
     * Obtiene una lista de todos los quizzes que pertenecen a un curso específico.
     * @param courseId El número de identificación del curso.
     * @return Una lista de quizzes de ese curso.
     */
    List<QuizResponse> getQuizzesByCourse(Long courseId);

    /**
     * Actualiza la información de un quiz que ya existe.
     * @param quizId El número de identificación del quiz a actualizar.
     * @param request El objeto con la nueva información.
     * @return El quiz actualizado.
     */
    QuizResponse updateQuiz(Long quizId, QuizCreateRequest request);

    /**
     * Borra un quiz y todas sus preguntas para siempre.
     * @param quizId El número de identificación del quiz que se va a borrar.
     */
    void deleteQuiz(Long quizId);

    /**
     * Agrega una nueva pregunta a un quiz que ya existe.
     * @param quizId El número de identificación del quiz.
     * @param request El objeto con la información de la pregunta a agregar.
     * @return El quiz completo, ya con la nueva pregunta.
     */
    QuizResponse addQuestion(Long quizId, QuestionCreateRequest request);

    /**
     * Busca un quiz que esté asociado a una lección específica.
     * @param lessonId El número de identificación de la lección.
     * @return El quiz de esa lección.
     */
    QuizResponse getQuizByLesson(Long lessonId);

    /**
     * Recibe las respuestas de un estudiante a un quiz, lo califica y guarda el resultado.
     * @param quizId El número de identificación del quiz que se está enviando.
     * @param request El objeto con las respuestas del estudiante.
     * @param user El estudiante que está enviando el quiz.
     * @return El resultado del quiz, incluyendo la calificación y si lo pasó o no.
     */
    QuizSubmissionResponse submitQuiz(Long quizId, QuizSubmissionRequest request, User user);
}