package learning.platform.service;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;
import learning.platform.entity.Lesson;
import learning.platform.mapper.LessonMapper;
import learning.platform.repository.LessonRepository;
import learning.platform.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para LessonServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private Course course;
    private Lesson lesson;
    private LessonCreateRequest request;
    private LessonResponse response;

    /**
     * Configura los objetos de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);

        lesson = new Lesson(request, course);
        lesson.setId(1L);
        lesson.setCourse(course);

        request = new LessonCreateRequest(1L, "Test Lesson", "https://example.com", null, 1, null);
        response = new LessonResponse(1L, 1L, "Test Lesson", "https://example.com", null, 1, null);
    }

    /**
     * Verifica que se pueda crear una lección correctamente.
     */
    @Test
    void shouldCreateLesson() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonMapper.toEntity(request)).thenReturn(lesson);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonMapper.toResponse(lesson)).thenReturn(response);

        LessonResponse result = lessonService.createLesson(request);

        verify(courseRepository).findById(1L);
        verify(lessonMapper).toEntity(request);
        verify(lessonRepository).save(lesson);
        verify(lessonMapper).toResponse(lesson);
        assertNotNull(result);
        assertEquals(response, result);
    }

    /**
     * Verifica que se lancen excepciones al crear una lección con curso no existente.
     */
    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnCreate() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> lessonService.createLesson(request));

        assertEquals("Curso no encontrado con ID: 1", exception.getMessage());
        verify(courseRepository).findById(1L);
        verifyNoInteractions(lessonMapper, lessonRepository);
    }

    /**
     * Verifica que se obtengan lecciones por curso, ordenadas por orderIndex.
     */
    @Test
    void shouldGetLessonsByCourseOrdered() {
        List<Lesson> lessons = Arrays.asList(
                new Lesson(request, course),
                new Lesson(new LessonCreateRequest(1L, "Lesson 2", "https://example.com/2", null, 2, null), course)
        );
        List<LessonResponse> responses = Arrays.asList(
                new LessonResponse(1L, 1L, "Test Lesson", "https://example.com", null, 1, null),
                new LessonResponse(2L, 1L, "Lesson 2", "https://example.com/2", null, 2, null)
        );

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);
        when(lessonMapper.toResponse(lessons.get(0))).thenReturn(responses.get(0));
        when(lessonMapper.toResponse(lessons.get(1))).thenReturn(responses.get(1));

        List<LessonResponse> result = lessonService.getLessonsByCourse(1L);

        verify(courseRepository).findById(1L);
        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        assertEquals(2, result.size());
        assertEquals(responses, result);
    }

    /**
     * Verifica que se actualice una lección correctamente.
     */
    @Test
    void shouldUpdateLesson() {
        Lesson updatedLesson = new Lesson(request, course);
        updatedLesson.setId(1L);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(updatedLesson);
        when(lessonMapper.toResponse(updatedLesson)).thenReturn(response);

        LessonResponse result = lessonService.updateLesson(1L, request);

        verify(lessonRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(lessonRepository).save(any(Lesson.class));
        verify(lessonMapper).toResponse(updatedLesson);
        assertEquals(response, result);
    }

    /**
     * Verifica que se elimine una lección correctamente.
     */
    @Test
    void shouldDeleteLesson() {
        when(lessonRepository.existsById(1L)).thenReturn(true);

        lessonService.deleteLesson(1L);

        verify(lessonRepository).existsById(1L);
        verify(lessonRepository).deleteById(1L);
    }

    /**
     * Verifica que se reordenen las lecciones correctamente.
     */
    @Test
    void shouldReorderLessons() {
        List<Lesson> lessons = Arrays.asList(
                new Lesson(request, course),
                new Lesson(new LessonCreateRequest(1L, "Lesson 2", "https://example.com/2", null, 2, null), course)
        );
        lessons.get(0).setId(1L);
        lessons.get(1).setId(2L);
        List<Long> newOrder = Arrays.asList(2L, 1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        lessonService.reorderLessons(1L, newOrder);

        verify(courseRepository).findById(1L);
        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        verify(lessonRepository, times(2)).save(any(Lesson.class));
    }

    /**
     * Verifica que se lance una excepción al reordenar con IDs inválidos.
     */
    @Test
    void shouldThrowExceptionWhenReorderingWithInvalidIds() {
        List<Lesson> lessons = Arrays.asList(
                new Lesson(request, course),
                new Lesson(new LessonCreateRequest(1L, "Lesson 2", "https://example.com/2", null, 2, null), course)
        );
        lessons.get(0).setId(1L);
        lessons.get(1).setId(2L);
        List<Long> invalidOrder = Arrays.asList(1L, 3L); // Incluye un ID inexistente (3L)

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> lessonService.reorderLessons(1L, invalidOrder));

        assertEquals("La lista de IDs proporcionada no coincide con las lecciones del curso", exception.getMessage());
        verify(courseRepository).findById(1L);
        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        verifyNoMoreInteractions(lessonRepository);
    }

    /**
     * Verifica que se lance una excepción al reordenar con curso no encontrado.
     */
    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnReorder() {
        List<Long> newOrder = Arrays.asList(1L, 2L);

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> lessonService.reorderLessons(1L, newOrder));

        assertEquals("Curso no encontrado con ID: 1", exception.getMessage());
        verify(courseRepository).findById(1L);
        verifyNoInteractions(lessonRepository, lessonMapper);
    }
}