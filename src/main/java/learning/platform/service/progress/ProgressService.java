package learning.platform.service.progress;

import learning.platform.dto.progress.ProgressUpdateRequest;

public interface ProgressService {
    ProgressResponse markCompleted(Integer enrollmentId, Integer lessonId);

    ProgressResponse updateProgress(Integer enrollmentId, Integer lessonId, ProgressUpdateRequest request);

    Float calculateCourseCompletionPercentage(Integer enrollmentId);
}
