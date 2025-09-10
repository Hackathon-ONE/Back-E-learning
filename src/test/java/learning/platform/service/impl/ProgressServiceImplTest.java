package learning.platform.service.impl;

import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.mapper.ProgressMapper;
import learning.platform.repository.ProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import learning.platform.testutil.TestDataFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgressServiceImplTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private ProgressMapper progressMapper;

    @InjectMocks
    private ProgressServiceImpl progressService;

    private Enrollment enrollment;
    private Lesson lesson1;
    private Lesson lesson2;
    private Progress progressCompleted;
    private Progress progressIncomplete;

    @BeforeEach
    void setUp() {
        // --- Helpers de TestDataFactory ---
        enrollment = TestDataFactory.buildEnrollment(1L);
        lesson1 = TestDataFactory.buildLessonWithId(1L);
        lesson2 = TestDataFactory.buildLessonWithId(2L);

        progressCompleted = TestDataFactory.buildProgress(enrollment, lesson1, true, 85);
        progressIncomplete = TestDataFactory.buildProgress(enrollment, lesson2, false, null);
    }

    @Test
    void markCompletedShouldSetProgressAsCompleted() {
        Progress progressToSave = TestDataFactory.buildProgress(enrollment, lesson1, false, null);

        // Simular que no existe progreso previo
        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson1)).thenReturn(null);
        when(progressMapper.toEntity(any())).thenReturn(progressToSave);
        when(progressRepository.save(progressToSave)).thenReturn(progressToSave);

        ProgressResponse result = progressService.markCompleted(enrollment.getId(), lesson1.getId());

        verify(progressRepository).save(progressToSave);
        // Verificar efectos sobre el entity
        assertTrue(progressToSave.getCompleted());
        assertEquals(BigDecimal.valueOf(100.0), progressToSave.getCompletionPercentage());
    }

    @Test
    void calculateCourseCompletionPercentageShouldReturnCorrectValue() {
        List<Progress> progresses = Arrays.asList(progressCompleted, progressIncomplete);

        when(progressRepository.findByEnrollment(enrollment)).thenReturn(progresses);

        Double percentage = progressService.calculateCourseCompletionPercentage(Math.toIntExact(enrollment.getId()));

        assertEquals(50.0, percentage);
    }

    @Test
    void markCompletedShouldReuseExistingProgressIfPresent() {
        // Simular progreso ya existente incompleto
        Progress existingProgress = TestDataFactory.buildProgress(enrollment, lesson1, false, null);

        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson1))
                .thenReturn(Optional.of(existingProgress));
        when(progressRepository.save(existingProgress)).thenReturn(existingProgress);

        ProgressResponse result = progressService.markCompleted(enrollment.getId(), lesson1.getId());

        verify(progressRepository).save(existingProgress);
        assertTrue(existingProgress.getCompleted());
        assertEquals(BigDecimal.valueOf(100.0), existingProgress.getCompletionPercentage());
    }
}
