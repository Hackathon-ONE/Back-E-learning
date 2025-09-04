package learning.platform.service;

import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Progress;
import learning.platform.mapper.ProgressMapper;
import learning.platform.repository.ProgressRepository;
import learning.platform.service.impl.ProgressServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProgressServiceTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private ProgressMapper progressMapper;

    @InjectMocks
    private ProgressServiceImpl progressService;

    public ProgressServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldMarkCompleted() {
        Integer enrollmentId = 1;
        Integer lessonId = 1;
        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        Progress progress = new Progress();
        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        ProgressResponse response = new ProgressResponse();

        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson)).thenReturn(null);
        when(progressMapper.toEntity(any())).thenReturn(progress);
        when(progressRepository.save(progress)).thenReturn(progress);
        when(progressMapper.toResponse(progress)).thenReturn(response);

        ProgressResponse result = progressService.markCompleted(enrollmentId, lessonId);

        verify(progressRepository).save(progress);
        assertTrue(result.getCompleted());
    }

    @Test
    void shouldCalculateCourseCompletionPercentage() {
        Integer enrollmentId = 1;
        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentId);
        List<Progress> progresses = Arrays.asList(
            new Progress() {{ setCompleted(true); }},
            new Progress() {{ setCompleted(false); }}
        );

        when(progressRepository.findByEnrollment(enrollment)).thenReturn(progresses);

        Float percentage = progressService.calculateCourseCompletionPercentage(enrollmentId);

        assertEquals(50.0f, percentage);
    }
}
