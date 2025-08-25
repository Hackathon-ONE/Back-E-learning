package learning.platform.lesson_tests;

import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.dto.lesson.LessonResponse;
import learning.platform.repository.LessonRepository;
import learning.platform.service.LessonServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonServiceImpl lessonService;

    public LessonServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateLesson() {
        LessonCreateRequest request = new LessonCreateRequest();
        Lesson lesson = new Lesson();
        LessonResponse response = new LessonResponse();

        when(lessonMapper.toEntity(request)).thenReturn(lesson);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonMapper.toResponse(lesson)).thenReturn(response);

        LessonResponse result = lessonService.createLesson(request);

        verify(lessonRepository).save(lesson);
        assertNotNull(result);
    }

    @Test
    void shouldGetLessonsByCourseOrdered() {
        Course course = new Course();
        course.setId(1);
        List<Lesson> lessons = Arrays.asList(new Lesson(), new Lesson());
        List<LessonResponse> responses = Arrays.asList(new LessonResponse(), new LessonResponse());

        when(lessonRepository.findByCourseOrderByOrderIndex(course)).thenReturn(lessons);
        when(lessonMapper.toResponse(any(Lesson.class))).thenReturn(responses.get(0), responses.get(1));

        List<LessonResponse> result = lessonService.getLessonsByCourse(1);

        verify(lessonRepository).findByCourseOrderByOrderIndex(course);
        assertEquals(2, result.size());
    }
}
