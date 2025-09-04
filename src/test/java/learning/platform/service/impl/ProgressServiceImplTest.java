package learning.platform.service;

import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.mapper.ProgressMapper;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.LessonRepository;
import learning.platform.repository.ProgressRepository;
import learning.platform.service.impl.ProgressServiceImpl;
import learning.platform.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private Progress progress;
    private ProgressResponse progressResponse;

    @BeforeEach
    void setUp() {
        enrollment = TestDataFactory.buildEnrollment(1L);
        lesson = TestDataFactory.buildLesson(1L, null, null); // nulls porque solo necesitamos ID
        progress = new Progress();
        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        progress.setCompleted(false);
        progress.setUpdatedAt(Instant.now());

        progressResponse = new ProgressResponse(1L, 1L, false, 0, Instant.now());
    }

    @Test
    void shouldMarkLessonAsCompleted() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson)).thenReturn(Optional.of(progress));
        when(progressMapper.toResponse(progress)).thenReturn(progressResponse);

        ProgressResponse result = progressService.markCompleted(1L, 1L);

        verify(progressRepository).save(progress);
        assertTrue(progress.isCompleted());
        assertNotNull(result);
        assertEquals(progressResponse, result);
    }

    @Test
    void shouldCreateProgressIfNotExistWhenMarkCompleted() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson)).thenReturn(Optional.empty());
        when(progressMapper.toResponse(any(Progress.class))).thenReturn(progressResponse);

        ProgressResponse result = progressService.markCompleted(1L, 1L);

        verify(progressRepository).save(any(Progress.class));
        assertNotNull(result);
        assertEquals(progressResponse, result);
    }

    @Test
    void shouldThrowWhenEnrollmentNotFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> progressService.markCompleted(1L, 1L));

        assertEquals("Enrollment no encontrado: 1", ex.getMessage());
    }

    @Test
    void shouldThrowWhenLessonNotFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> progressService.markCompleted(1L, 1L));

        assertEquals("Lesson no encontrada: 1", ex.getMessage());
    }

    @Test
    void shouldGetProgress() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson)).thenReturn(Optional.of(progress));
        when(progressMapper.toResponse(progress)).thenReturn(progressResponse);

        ProgressResponse result = progressService.getProgress(1L, 1L);

        assertEquals(progressResponse, result);
    }

    @Test
    void shouldThrowWhenProgressNotFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> progressService.getProgress(1L, 1L));

        assertEquals("Progress no encontrado para enrollment y lesson dados", ex.getMessage());
    }

    @Test
    void shouldGetAllProgressByEnrollment() {
        List<Progress> progresses = List.of(progress);
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(progressRepository.findByEnrollment(enrollment)).thenReturn(progresses);
        when(progressMapper.toResponse(progress)).thenReturn(progressResponse);

        List<ProgressResponse> result = progressService.getAllProgressByEnrollment(1L);

        assertEquals(1, result.size());
        assertEquals(progressResponse, result.get(0));
    }
}
