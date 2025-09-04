package learning.platform.service;

import learning.platform.dto.ProgressUpdateRequest;
import learning.platform.dto.ProgressResponse;

import java.util.List;

public interface ProgressService {

    ProgressResponse markCompleted(Long enrollmentId, Long lessonId);

    ProgressResponse getProgress(Long enrollmentId, Long lessonId);

    List<ProgressResponse> getAllProgressByEnrollment(Long enrollmentId);

    ProgressResponse createOrUpdateProgress(ProgressUpdateRequest request);

    List<ProgressResponse> getProgressByEnrollment(Long enrollmentId);

    Double calculateCourseCompletionPercentage(Integer enrollmentId);
}
