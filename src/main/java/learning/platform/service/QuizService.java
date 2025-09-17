package learning.platform.service;

import learning.platform.dto.*;
import java.util.List;

public interface QuizService {

    QuizResponse createQuiz(QuizCreateRequest request);

    QuizResponse getQuizById(Long quizId);

    List<QuizResponse> getQuizzesByCourse(Long courseId);

    QuizResponse updateQuiz(Long quizId, QuizCreateRequest request);

    void deleteQuiz(Long quizId);

    QuizResponse addQuestion(Long quizId, QuestionCreateRequest request);

    QuizResponse getQuizByLesson(Long lessonId);

    QuizSubmissionResponse submitQuiz(Long quizId, QuizSubmissionRequest request);
}
