package learning.platform.service;

import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.mapper.ProgressMapper;
import learning.platform.repository.ProgressRepository;
import learning.platform.service.impl.ProgressServiceImpl;
import learning.platform.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgressServiceTest {

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
        enrollment = TestDataFactory.buildEnrollment(1L);
        lesson1 = TestDataFactory.buildLessonWithId(1L);
        lesson2 = TestDataFactory.buildLessonWithId(2L);

        progressCompleted = TestDataFactory.buildProgress(enrollment, lesson1, true, 90);
        progressIncomplete = TestDataFactory.buildProgress(enrollment, lesson2, false, null);
    }

    @Test
    void shouldMarkProgressAsCompleted() {
        Progress progressToSave = TestDataFactory.buildProgress(enrollment, lesson1, false, null);

        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson1))
                .thenReturn(null); // o Optional.empty() según la implementación
        when(progressMapper.toEntity(any())).thenReturn(progressToSave);
        when(progressRepository.save(progressToSave)).thenReturn(progressToSave);

        ProgressResponse result = progressService.markCompleted(enrollment.getId(), lesson1.getId());

        verify(progressRepository).save(progressToSave);
        // Verifico efectos sobre el entity, no sobre DTO
        assertTrue(progressToSave.getCompleted());
        assertEquals(BigDecimal.valueOf(100.0), progressToSave.getCompletionPercentage());
    }

    @Test
    void shouldCalculateCourseCompletionPercentage() {
        List<Progress> progresses = Arrays.asList(progressCompleted, progressIncomplete);

        when(progressRepository.findByEnrollment(enrollment)).thenReturn(progresses);

        Double percentage = progressService.calculateCourseCompletionPercentage(Math.toIntExact(enrollment.getId()));

        assertEquals(50.0, percentage);
    }
}
