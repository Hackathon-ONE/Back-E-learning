package learning.platform.service.impl;

import learning.platform.dto.*;
import learning.platform.entity.*;
import learning.platform.exception.NotFoundException;
import learning.platform.repository.*;
import learning.platform.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for QuizServiceImpl")
public class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerOptionRepository answerOptionRepository;
    @Mock
    private QuizSubmissionRepository quizSubmissionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private QuizServiceImpl quizService;

    // Entidades de prueba
    private Quiz quiz;
    private User studentUser;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        // Configuración de las entidades base para las pruebas
        course = new Course();
        course.setId(1L);

        quiz = new Quiz();
        quiz.setId(10L);
        quiz.setTitle("Test Quiz");
        quiz.setDescription("A basic quiz for testing.");
        quiz.setCourse(course);

        studentUser = new User();
        studentUser.setId(1L);
        studentUser.setFullName("John Doe");

        enrollment = new Enrollment();
        enrollment.setId(100L);
        enrollment.setStudent(studentUser);
        enrollment.setCourse(course);
    }

    // --- Pruebas para createQuiz ---
    @Test
    @DisplayName("Should create a quiz successfully")
    void shouldCreateQuizSuccessfully() {
        // Simula el comportamiento del repositorio al guardar
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);
        when(questionRepository.saveAll(any())).thenReturn(Collections.emptyList());

        // Crea el DTO de solicitud
        QuizCreateRequest request = new QuizCreateRequest();
        request.setTitle("Test Quiz");
        request.setDescription("A basic quiz.");
        request.setCourseId(1L);
        request.setQuestions(new ArrayList<>()); // Preguntas simuladas

        QuizResponse response = quizService.createQuiz(request);

        assertNotNull(response);
        assertEquals(quiz.getId(), response.getId());
        assertEquals(quiz.getTitle(), response.getTitle());
    }

    @Test
    @DisplayName("Should throw NotFoundException when creating quiz with non-existent course")
    void shouldThrowNotFoundExceptionForNonExistentCourse() {
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        QuizCreateRequest request = new QuizCreateRequest();
        request.setTitle("Test Quiz");
        request.setCourseId(99L); // ID que no existe

        assertThrows(NotFoundException.class, () -> quizService.createQuiz(request));
    }

    // --- Pruebas para getQuizById ---
    @Test
    @DisplayName("Should retrieve a quiz by ID")
    void shouldGetQuizByIdSuccessfully() {
        when(quizRepository.findById(10L)).thenReturn(Optional.of(quiz));
        when(questionRepository.findByQuizId(10L)).thenReturn(Collections.emptyList());

        QuizResponse response = quizService.getQuizById(10L);

        assertNotNull(response);
        assertEquals(quiz.getId(), response.getId());
    }

    @Test
    @DisplayName("Should throw NotFoundException when quiz ID does not exist")
    void shouldThrowNotFoundExceptionWhenQuizNotFound() {
        when(quizRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> quizService.getQuizById(99L));
    }

    // --- Pruebas para submitQuiz ---
    @Test
    @DisplayName("Should submit a quiz and return a passing score")
    void shouldSubmitQuizAndReturnPassingScore() {
        // Mock de datos para la prueba
        Question q1 = new Question();
        q1.setId(1L);
        q1.setText("Q1");
        q1.setPoints(10);
        q1.setCorrectOptionIndex(0);

        AnswerOption correctOption = new AnswerOption();
        correctOption.setId(101L);
        correctOption.setText("Correct");
        correctOption.setQuestion(q1);

        AnswerOption incorrectOption = new AnswerOption();
        incorrectOption.setId(102L);
        incorrectOption.setText("Incorrect");
        incorrectOption.setQuestion(q1);

        List<AnswerOption> options = List.of(correctOption, incorrectOption);
        q1.setOptions(options);

        // Simula el comportamiento del repositorio
        when(quizRepository.findById(any(Long.class))).thenReturn(Optional.of(quiz));
        when(enrollmentRepository.existsByStudentIdAndCourseId(any(Long.class), any(Long.class))).thenReturn(true);
        when(questionRepository.findByQuizId(any(Long.class))).thenReturn(List.of(q1));
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(answerOptionRepository.findById(correctOption.getId())).thenReturn(Optional.of(correctOption));
        when(quizSubmissionRepository.save(any(QuizSubmission.class))).thenAnswer(i -> i.getArguments()[0]);

        // Crea el DTO de solicitud de envío con la respuesta correcta
        QuizSubmissionRequest request = new QuizSubmissionRequest();
        AnswerSubmissionItem item = new AnswerSubmissionItem();
        item.setQuestionId(q1.getId());
        item.setSelectedOptionId(correctOption.getId());
        request.setAnswers(List.of(item));

        QuizSubmissionResponse response = quizService.submitQuiz(quiz.getId(), request, studentUser);

        assertNotNull(response);
        assertTrue(response.getPassed());
        assertEquals(100.0, response.getPercentage());
        assertEquals(1, response.getScore());
    }

    @Test
    @DisplayName("Should submit a quiz and return a failing score")
    void shouldSubmitQuizAndReturnFailingScore() {
        // Mock de datos para la prueba
        Question q1 = new Question();
        q1.setId(1L);
        q1.setText("Q1");
        q1.setPoints(10);
        q1.setCorrectOptionIndex(0);

        AnswerOption correctOption = new AnswerOption();
        correctOption.setId(101L);
        correctOption.setText("Correct");
        correctOption.setQuestion(q1);

        AnswerOption incorrectOption = new AnswerOption();
        incorrectOption.setId(102L);
        incorrectOption.setText("Incorrect");
        incorrectOption.setQuestion(q1);

        List<AnswerOption> options = List.of(correctOption, incorrectOption);
        q1.setOptions(options);

        // Simula el comportamiento del repositorio
        when(quizRepository.findById(any(Long.class))).thenReturn(Optional.of(quiz));
        when(enrollmentRepository.existsByStudentIdAndCourseId(any(Long.class), any(Long.class))).thenReturn(true);
        when(questionRepository.findByQuizId(any(Long.class))).thenReturn(List.of(q1));
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(answerOptionRepository.findById(incorrectOption.getId())).thenReturn(Optional.of(incorrectOption));
        when(quizSubmissionRepository.save(any(QuizSubmission.class))).thenAnswer(i -> i.getArguments()[0]);

        // Crea el DTO de solicitud de envío con la respuesta INCORRECTA
        QuizSubmissionRequest request = new QuizSubmissionRequest();
        AnswerSubmissionItem item = new AnswerSubmissionItem();
        item.setQuestionId(q1.getId());
        item.setSelectedOptionId(incorrectOption.getId());
        request.setAnswers(List.of(item));

        QuizSubmissionResponse response = quizService.submitQuiz(quiz.getId(), request, studentUser);

        assertNotNull(response);
        assertFalse(response.getPassed());
        assertEquals(0.0, response.getPercentage());
        assertEquals(0, response.getScore());
    }

    @Test
    @DisplayName("Should throw IllegalStateException if student is not enrolled in the course")
    void shouldThrowExceptionIfStudentNotEnrolled() {
        when(quizRepository.findById(any(Long.class))).thenReturn(Optional.of(quiz));
        when(enrollmentRepository.existsByStudentIdAndCourseId(any(Long.class), any(Long.class))).thenReturn(false);

        QuizSubmissionRequest request = new QuizSubmissionRequest();

        assertThrows(IllegalStateException.class, () -> quizService.submitQuiz(quiz.getId(), request, studentUser));
    }

    // --- Pruebas para deleteQuiz ---
    @Test
    @DisplayName("Should delete a quiz successfully")
    void shouldDeleteQuizSuccessfully() {
        when(quizRepository.existsById(10L)).thenReturn(true);

        quizService.deleteQuiz(10L);

        Mockito.verify(quizRepository, Mockito.times(1)).deleteById(10L);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existent quiz")
    void shouldThrowNotFoundExceptionForNonExistentQuiz() {
        when(quizRepository.existsById(any(Long.class))).thenReturn(false);

        assertThrows(NotFoundException.class, () -> quizService.deleteQuiz(99L));
    }
}