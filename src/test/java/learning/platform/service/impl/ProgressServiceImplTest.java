package learning.platform.service.impl;

import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.mapper.ProgressMapper;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.LessonRepository;
import learning.platform.repository.ProgressRepository;
import learning.platform.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProgressServiceImplTest {

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void markCompleted_createsNewProgressIfNotExists() {
        Enrollment enrollment = TestDataFactory.buildEnrollment(1L);
        Lesson lesson = TestDataFactory.buildLessonWithId(1L);
        ProgressResponse expectedResponse = new ProgressResponse(
                1L, 1L, 1L, true, null, Instant.now()
        );

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson)).thenReturn(null);
        when(progressRepository.save(any(Progress.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(progressMapper.toResponse(any(Progress.class))).thenReturn(expectedResponse);

        ProgressResponse response = progressService.markCompleted(1L, 1L);

        assertEquals(expectedResponse, response);
        verify(progressRepository).save(any(Progress.class));
    }

    @Test
    void getProgress_returnsProgressResponseIfExists() {
        Enrollment enrollment = TestDataFactory.buildEnrollment(1L);
        Lesson lesson = TestDataFactory.buildLessonWithId(1L);
        Progress progress = TestDataFactory.buildProgress(enrollment, lesson, true, 90);
        ProgressResponse expectedResponse = new ProgressResponse(
                1L, 1L, 1L, true, 90, Instant.now()
        );

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollmentAndLesson(enrollment, lesson)).thenReturn(Optional.of(progress));
        when(progressMapper.toResponse(progress)).thenReturn(expectedResponse);

        ProgressResponse response = progressService.getProgress(1L, 1L);

        assertEquals(expectedResponse, response);
    }

    @Test
    void getAllProgressByEnrollment_returnsListOfResponses() {
        Enrollment enrollment = TestDataFactory.buildEnrollment(1L);
        Lesson lesson = TestDataFactory.buildLessonWithId(1L);
        Progress progress = TestDataFactory.buildProgress(enrollment, lesson, true, null);
        ProgressResponse expectedResponse = new ProgressResponse(
                1L, 1L, 1L, true, null, Instant.now()
        );

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(progressRepository.findByEnrollment(enrollment)).thenReturn(List.of(progress));
        when(progressMapper.toResponse(progress)).thenReturn(expectedResponse);

        List<ProgressResponse> responses = progressService.getAllProgressByEnrollment(1L);

        assertEquals(1, responses.size());
        assertEquals(expectedResponse, responses.get(0));
    }
}
