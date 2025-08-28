package learning.platform.progress;

import learning.platform.dto.progress.ProgressResponse;
import learning.platform.dto.progress.ProgressUpdateRequest;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.mapper.ProgressMapper;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.LessonRepository;
import learning.platform.repository.ProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgressServiceImplTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ProgressMapper progressMapper;

    @InjectMocks
    private ProgressServiceImpl progressService;

    private Enrollment enrollment;
    private Lesson lesson;
    private ProgressUpdateRequest request;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment();
        enrollment.setId(1L);
        lesson = new Lesson();
        lesson.setId(1L);
        request = new ProgressUpdateRequest(1L, 1L, true, 85, Instant.now());
    }

    @Test
    void shouldMarkCompleted() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson)).thenReturn(null);
        Progress progress = new Progress();
        progress.setId(1L);
        progress.setCompleted(true);
        when(progressRepository.save(any(Progress.class))).thenReturn(progress);
        when(progressMapper.toResponse(progress)).thenReturn(new ProgressResponse(1L, 1L, 1L, true, null, Instant.now()));

        var response = progressService.markCompleted(1L, 1L);

        assertEquals(true, response.completed());
        verify(progressRepository).save(any(Progress.class));
    }

    @Test
    void shouldThrowExceptionWhenEnrollmentNotFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> progressService.markCompleted(1L, 1L));
    }

    @Test
    void shouldCalculateCompletionPercentage() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(progressRepository.findByEnrollment(enrollment)).thenReturn(Collections.singletonList(new Progress()));
        when(lessonRepository.countByCourse(enrollment.getCourse())).thenReturn(1L);

        Double percentage = progressService.calculateCourseCompletionPercentage(1L);

        assertEquals(100.0, percentage);
    }
}