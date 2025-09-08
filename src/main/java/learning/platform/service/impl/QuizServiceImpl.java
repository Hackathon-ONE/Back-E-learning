package learning.platform.service.impl;

import learning.platform.dto.*;
import learning.platform.service.QuizService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    @Override
    public QuizResponse createQuiz(QuizCreateRequest request) {
        QuizResponse quiz = new QuizResponse();
        quiz.setId(1L); // Simulación: en realidad lo haría la BD
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCourseId(request.getCourseId());
        quiz.setQuestions(new ArrayList<>());
        return quiz;
    }

    @Override
    public QuizResponse getQuizById(Long quizId) {
        // Simulación: en un caso real buscaría en la BD
        QuizResponse quiz = new QuizResponse();
        quiz.setId(quizId);
        quiz.setTitle("Quiz ejemplo");
        quiz.setDescription("Descripción del quiz");
        quiz.setCourseId(10L);
        quiz.setQuestions(new ArrayList<>());
        return quiz;
    }

    @Override
    public List<QuizResponse> getQuizzesByCourse(Long courseId) {
        List<QuizResponse> quizzes = new ArrayList<>();
        quizzes.add(getQuizById(1L));
        return quizzes;
    }

    @Override
    public QuizResponse updateQuiz(Long quizId, QuizCreateRequest request) {
        QuizResponse quiz = getQuizById(quizId);
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCourseId(request.getCourseId());
        return quiz;
    }

    @Override
    public void deleteQuiz(Long quizId) {
        // Simulación: eliminar de la BD
    }

    @Override
    public QuizResponse addQuestion(Long quizId, QuestionCreateRequest request) {
        QuizResponse quiz = getQuizById(quizId);

        if (quiz.getQuestions() == null) {
            quiz.setQuestions(new ArrayList<>());
        }

        // Convertimos opciones de creación a opciones de respuesta (DTO)
        List<AnswerOptionDTO> optionDTOs = request.getOptions()
                .stream()
                .map(opt -> new AnswerOptionDTO(null, opt.getText())) // ID null simulado
                .collect(Collectors.toList());

        QuestionResponse question = new QuestionResponse();
        question.setId(100L); // Simulado
        question.setText(request.getText());
        question.setOptions(optionDTOs);
        question.setPoints(request.getPoints());
        question.setTimeLimitSeconds(request.getTimeLimitSeconds());

        quiz.getQuestions().add(question);
        return quiz;
    }

    @Override
    public QuizResponse getQuizByLesson(Long lessonId) {
        QuizResponse quiz = new QuizResponse();
        quiz.setId(200L);
        quiz.setTitle("Quiz de la lección " + lessonId);
        quiz.setDescription("Preguntas para reforzar la lección");
        quiz.setCourseId(20L);
        quiz.setQuestions(new ArrayList<>());
        return quiz;
    }

    @Override
    public QuizSubmissionResponse submitQuiz(Long quizId, QuizSubmissionRequest request) {
        QuizResponse quiz = getQuizById(quizId);

        int totalQuestions = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;
        int correct = request.getAnswers() != null ? request.getAnswers().size() : 0; // simulado

        QuizSubmissionResponse response = new QuizSubmissionResponse();
        response.setQuizId(quizId);
        response.setEnrollmentId(request.getEnrollmentId());
        response.setScore(correct);
        response.setPercentage(totalQuestions > 0 ? (correct * 100.0 / totalQuestions) : 0.0);
        response.setPassed(response.getPercentage() >= 60.0);

        return response;
    }

}
