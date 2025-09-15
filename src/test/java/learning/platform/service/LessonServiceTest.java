package learning.platform.service;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;
import learning.platform.entity.Course;
import learning.platform.entity.Lesson;
import learning.platform.mapper.LessonMapper;
import learning.platform.repository.CourseRepository;
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

import static learning.platform.testutil.TestDataFactory.buildCourse;
import static learning.platform.testutil.TestDataFactory.buildLesson;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

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

    @BeforeEach
    void setUp() {
        course = buildCourse(1L);
        request = new LessonCreateRequest("TestLesson", 1, 30);
        response = new LessonResponse(1L, 1L, "Test Lesson", 1, null);
        lesson = buildLesson(1L, request, course);
    }

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

    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnCreate() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> lessonService.createLesson(request));

        assertEquals("Curso no encontrado con ID: 1", exception.getMessage());
        verify(courseRepository).findById(1L);
        verifyNoInteractions(lessonMapper, lessonRepository);
    }

    @Test
    void shouldGetLessonsByCourseOrdered() {
        Lesson lesson2 = buildLesson(2L, new LessonCreateRequest(1L, "Lesson 2", "https://example.com/2", null, 2, 25), course);
        List<Lesson> lessons = Arrays.asList(lesson, lesson2);

        List<LessonResponse> responses = Arrays.asList(
                new LessonResponse(1L, 1L, "Test Lesson", "https://example.com", null, 1, null),
                new LessonResponse(2L, 1L, "Lesson 2", "https://example.com/2", null, 2, null)
        );

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);
        when(lessonMapper.toResponse(lesson)).thenReturn(responses.get(0));
        when(lessonMapper.toResponse(lesson2)).thenReturn(responses.get(1));

        List<LessonResponse> result = lessonService.getLessonsByCourse(1L);

        verify(courseRepository).findById(1L);
        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        assertEquals(2, result.size());
        assertEquals(responses, result);
    }

    @Test
    void shouldUpdateLesson() {
        Lesson updatedLesson = buildLesson(1L, request, course);

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

    @Test
    void shouldDeleteLesson() {
        when(lessonRepository.existsById(1L)).thenReturn(true);

        lessonService.deleteLesson(1L);

        verify(lessonRepository).existsById(1L);
        verify(lessonRepository).deleteById(1L);
    }

    @Test
    void shouldReorderLessons() {
        Lesson lesson2 = buildLesson(2L, new LessonCreateRequest(1L, "Lesson 2", "https://example.com/2", null, 2, 25), course);
        List<Lesson> lessons = Arrays.asList(lesson, lesson2);
        List<Long> newOrder = Arrays.asList(2L, 1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        lessonService.reorderLessons(1L, newOrder);

        verify(courseRepository).findById(1L);
        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        verify(lessonRepository, times(2)).save(any(Lesson.class));
    }
}
