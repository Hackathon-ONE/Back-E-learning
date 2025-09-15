package learning.platform.service.impl;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;
import learning.platform.entity.Course;
import learning.platform.entity.Lesson;
import learning.platform.mapper.LessonMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.LessonRepository;
import learning.platform.testutil.TestDataFactory;
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
    private LessonCreateRequest request;
    private LessonResponse response;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        course = TestDataFactory.buildCourse(1L);
        request = new LessonCreateRequest(
                "Test Lesson",
                1,
                60
                );
        lesson = TestDataFactory.buildLesson(1L, request, course);
        response = new LessonResponse(
                1L,
                1L,
                "Test Lesson",
                1,
                60
        );
    }

    @Test
    void shouldCreateLesson() {
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(lessonMapper.toEntity(request)).thenReturn(lesson);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonMapper.toResponse(lesson)).thenReturn(response);

        LessonResponse result = lessonService.createLesson(1L, request);

        verify(courseRepository).findById(course.getId());
        verify(lessonMapper).toEntity(request);
        verify(lessonRepository).save(lesson);
        verify(lessonMapper).toResponse(lesson);
        assertNotNull(result);
        assertEquals(response, result);
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnCreate() {
        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> lessonService.createLesson(1L, request));

        assertEquals("Curso no encontrado con ID: " + course.getId(), exception.getMessage());
        verify(courseRepository).findById(course.getId());
        verifyNoInteractions(lessonMapper, lessonRepository);
    }

    @Test
    void shouldGetLessonsByCourseOrdered() {
        Lesson lesson2 = TestDataFactory.buildLesson(
                2L,
                new LessonCreateRequest(
                        "Lesson 2",
                        2,
                        50),
                course
        );

        List<Lesson> lessons = Arrays.asList(lesson, lesson2);
        List<LessonResponse> responses = Arrays.asList(
                response,
                new LessonResponse(
                        2L,
                        course.getId(),
                        "Lesson 2",
                        2,
                        50
                )
        );

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);
        when(lessonMapper.toResponse(lesson)).thenReturn(responses.get(0));
        when(lessonMapper.toResponse(lesson2)).thenReturn(responses.get(1));

        List<LessonResponse> result = lessonService.getLessonsByCourse(course.getId());

        verify(courseRepository).findById(course.getId());
        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        assertEquals(2, result.size());
        assertEquals(responses, result);
    }

    @Test
    void shouldUpdateLesson() {
        Lesson updatedLesson = TestDataFactory.buildLesson(1L, request, course);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(updatedLesson);
        when(lessonMapper.toResponse(updatedLesson)).thenReturn(response);

        LessonResponse result = lessonService.updateLesson(1L, request);

        verify(lessonRepository).findById(1L);
        verify(courseRepository).findById(course.getId());
        verify(lessonRepository).save(any(Lesson.class));
        verify(lessonMapper).toResponse(updatedLesson);
        assertEquals(response, result);
    }

    @Test
    void shouldDeleteLesson() {
        when(lessonRepository.existsById(1L)).thenReturn(true);

        lessonService.deleteLesson(1L);

        verify(lessonRepository).existsById(1L);
        verify(lessonRepository).deleteById(1L);
    }

    @Test
    void shouldReorderLessons() {
        Lesson lesson2 = TestDataFactory.buildLesson(
                2L,
                new LessonCreateRequest(
                        "Lesson 2",
                        2,
                        50
                ),
                course
        );

        List<Lesson> lessons = Arrays.asList(lesson, lesson2);
        List<Long> newOrder = Arrays.asList(2L, 1L);

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        lessonService.reorderLessons(course.getId(), newOrder);

        verify(courseRepository).findById(course.getId());
        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        verify(lessonRepository, times(2)).save(any(Lesson.class));
    }

    @Test
    void shouldThrowExceptionWhenReorderingWithInvalidIds() {
        Lesson lesson2 = TestDataFactory.buildLesson(
                2L,
                new LessonCreateRequest(
                        "Lesson 2",
                        2,
                        50
                ),
                course
        );

        List<Lesson> lessons = Arrays.asList(lesson, lesson2);
        List<Long> invalidOrder = Arrays.asList(1L, 3L);

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> lessonService.reorderLessons(course.getId(), invalidOrder));

        assertEquals("La lista de IDs proporcionada no coincide con las lecciones del curso", exception.getMessage());
        verify(courseRepository).findById(course.getId());
        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnReorder() {
        List<Long> newOrder = Arrays.asList(1L, 2L);

        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> lessonService.reorderLessons(course.getId(), newOrder));

        assertEquals("Curso no encontrado con ID: " + course.getId(), exception.getMessage());
        verify(courseRepository).findById(course.getId());
        verifyNoInteractions(lessonRepository, lessonMapper);
    }
}
