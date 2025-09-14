package learning.platform.service.impl;

import learning.platform.dto.*;
import learning.platform.entity.*;
import learning.platform.exception.NotFoundException;
import learning.platform.repository.*;
import learning.platform.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final AnswerSubmissionRepository answerSubmissionRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;

    public QuizServiceImpl(QuizRepository quizRepository,
                           QuestionRepository questionRepository,
                           AnswerOptionRepository answerOptionRepository,
                           QuizSubmissionRepository quizSubmissionRepository,
                           AnswerSubmissionRepository answerSubmissionRepository,
                           UserRepository userRepository,
                           CourseRepository courseRepository,
                           LessonRepository lessonRepository,
                           EnrollmentRepository enrollmentRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.answerSubmissionRepository = answerSubmissionRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    @Transactional
    public QuizResponse createQuiz(QuizCreateRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());

        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new NotFoundException("No se encontró el id del curso: " + request.getCourseId()));
            quiz.setCourse(course);
        }

        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId())
                    .orElseThrow(() -> new NotFoundException("No se encontró el id de la lección: " + request.getLessonId()));
            quiz.setLesson(lesson);
        }

        Quiz savedQuiz = quizRepository.save(quiz);

        List<Question> questions = request.getQuestions().stream()
                .map(questionDto -> {
                    Question question = new Question();
                    question.setQuiz(savedQuiz);
                    question.setText(questionDto.getText());
                    question.setPoints(questionDto.getPoints());
                    question.setTimeLimitSeconds(questionDto.getTimeLimitSeconds());
                    question.setCorrectOptionIndex(questionDto.getCorrectOptionIndex());

                    List<AnswerOption> options = questionDto.getOptions().stream()
                            .map(optionDto -> {
                                AnswerOption option = new AnswerOption();
                                option.setText(optionDto.getText());
                                option.setQuestion(question);
                                return option;
                            }).collect(Collectors.toList());

                    question.setOptions(options);
                    return question;
                }).collect(Collectors.toList());

        questionRepository.saveAll(questions);

        return convertToQuizResponse(savedQuiz);
    }

    @Override
    public QuizResponse getQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found with id: " + quizId));
        return convertToQuizResponse(quiz);
    }

    @Override
    public List<QuizResponse> getQuizzesByCourse(Long courseId) {
        List<Quiz> quizzes = quizRepository.findByCourseId(courseId);
        return quizzes.stream()
                .map(this::convertToQuizResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuizResponse updateQuiz(Long quizId, QuizCreateRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found with id: " + quizId));

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        // Lógica para actualizar preguntas, no implementada aquí para simplicidad

        Quiz updatedQuiz = quizRepository.save(quiz);
        return convertToQuizResponse(updatedQuiz);
    }

    @Override
    @Transactional
    public void deleteQuiz(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new NotFoundException("Quiz not found with id: " + quizId);
        }
        quizRepository.deleteById(quizId);
    }

    @Override
    @Transactional
    public QuizResponse addQuestion(Long quizId, QuestionCreateRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found with id: " + quizId));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(request.getText());
        question.setCorrectOptionIndex(request.getCorrectOptionIndex());
        question.setPoints(request.getPoints());
        question.setTimeLimitSeconds(request.getTimeLimitSeconds());

        List<AnswerOption> options = request.getOptions().stream()
                .map(optionDto -> {
                    AnswerOption option = new AnswerOption();
                    option.setText(optionDto.getText());
                    option.setQuestion(question);
                    return option;
                }).collect(Collectors.toList());

        question.setOptions(options);
        questionRepository.save(question);

        return convertToQuizResponse(quiz);
    }

    // Método que faltaba, ahora implementado
    @Override
    public QuizResponse getQuizByLesson(Long lessonId) {
        Quiz quiz = quizRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new NotFoundException("Quiz not found for lesson id: " + lessonId));
        return convertToQuizResponse(quiz);
    }

    @Override
    @Transactional
    public QuizSubmissionResponse submitQuiz(Long quizId, QuizSubmissionRequest request, User user) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found with id: " + quizId));

        if (quiz.getCourse() != null) {
            boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(user.getId(), quiz.getCourse().getId());
            if (!isEnrolled) {
                throw new IllegalStateException("User is not enrolled in this course and cannot submit the quiz.");
            }
        }

        List<Question> questions = questionRepository.findByQuizId(quizId);

        QuizSubmission submission = new QuizSubmission();
        submission.setQuiz(quiz);
        submission.setUser(user);

        List<AnswerSubmission> submittedAnswers = request.getAnswers().stream()
                .map(answerDto -> {
                    Question question = questionRepository.findById(answerDto.getQuestionId())
                            .orElseThrow(() -> new NotFoundException("Question not found with id: " + answerDto.getQuestionId()));
                    AnswerOption selectedOption = answerOptionRepository.findById(answerDto.getSelectedOptionId())
                            .orElseThrow(() -> new NotFoundException("Answer option not found with id: " + answerDto.getSelectedOptionId()));

                    AnswerSubmission submissionItem = new AnswerSubmission();
                    submissionItem.setQuizSubmission(submission);
                    submissionItem.setQuestion(question);
                    submissionItem.setSelectedOption(selectedOption);
                    return submissionItem;
                }).collect(Collectors.toList());

        int correctCount = 0;
        int totalPoints = 0;
        for (Question question : questions) {
            totalPoints += (question.getPoints() != null) ? question.getPoints() : 0;
            Long correctAnswerOptionId = getCorrectAnswerOptionId(question);

            AnswerSubmission studentAnswer = submittedAnswers.stream()
                    .filter(sa -> sa.getQuestion().getId().equals(question.getId()))
                    .findFirst()
                    .orElse(null);

            if (studentAnswer != null && studentAnswer.getSelectedOption() != null) {
                if (studentAnswer.getSelectedOption().getId().equals(correctAnswerOptionId)) {
                    correctCount++;
                }
            }
        }

        submission.setAnswers(submittedAnswers);

        int totalQuestions = questions.size();
        double percentage = (totalQuestions > 0) ? ((double) correctCount / totalQuestions) * 100 : 0.0;
        submission.setScore(correctCount);
        submission.setPercentage(percentage);
        submission.setPassed(percentage >= 60.0);

        quizSubmissionRepository.save(submission);

        return convertToQuizSubmissionResponse(submission);
    }

    // Método auxiliar para obtener el ID de la opción de respuesta correcta
    private Long getCorrectAnswerOptionId(Question question) {
        if (question.getCorrectOptionIndex() != null && question.getCorrectOptionIndex() >= 0 && question.getCorrectOptionIndex() < question.getOptions().size()) {
            return question.getOptions().get(question.getCorrectOptionIndex()).getId();
        }
        return null;
    }

    // Métodos auxiliares para la conversión de entidades a DTOs
    private QuizResponse convertToQuizResponse(Quiz quiz) {
        QuizResponse dto = new QuizResponse();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setCourseId(quiz.getCourse() != null ? quiz.getCourse().getId() : null);
        dto.setLessonId(quiz.getLesson() != null ? quiz.getLesson().getId() : null);

        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        List<QuestionResponse> questionDtos = questions.stream()
                .map(this::convertToQuestionResponse)
                .collect(Collectors.toList());
        dto.setQuestions(questionDtos);

        return dto;
    }

    private QuestionResponse convertToQuestionResponse(Question question) {
        QuestionResponse dto = new QuestionResponse();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setPoints(question.getPoints());
        dto.setTimeLimitSeconds(question.getTimeLimitSeconds());

        List<AnswerOption> options = answerOptionRepository.findByQuestionId(question.getId());
        List<AnswerOptionDTO> optionDtos = options.stream()
                .map(this::convertToAnswerOptionDTO)
                .collect(Collectors.toList());
        dto.setOptions(optionDtos);

        return dto;
    }

    private AnswerOptionDTO convertToAnswerOptionDTO(AnswerOption option) {
        AnswerOptionDTO dto = new AnswerOptionDTO();
        dto.setId(option.getId());
        dto.setText(option.getText());
        return dto;
    }

    private QuizSubmissionResponse convertToQuizSubmissionResponse(QuizSubmission submission) {
        QuizSubmissionResponse dto = new QuizSubmissionResponse();
        dto.setQuizId(submission.getQuiz().getId());
        dto.setScore(submission.getScore());
        dto.setPercentage(submission.getPercentage());
        dto.setPassed(submission.getPassed());
        return dto;
    }
}