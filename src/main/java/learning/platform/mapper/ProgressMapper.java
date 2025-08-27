package learning.platform.mapper;

import learning.platform.dto.progress.ProgressUpdateRequest;
import learning.platform.dto.progress.ProgressResponse;
import learning.platform.entity.Progress;
import learning.platform.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class ProgressMapper {

    public Progress toEntity(ProgressUpdateRequest request, Enrollment enrollment, Lesson lesson) {
        Progress progress = new Progress();
        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        progress.setCompleted(request.getCompleted());
        progress.setScore(request.getScore());
        progress.setUpdatedAt(request.getUpdatedAt());
        return progress;
    }

    public void updateEntityFromRequest(Progress progress, ProgressUpdateRequest request) {
        progress.setCompleted(request.getCompleted());
        progress.setScore(request.getScore());
        progress.setUpdatedAt(request.getUpdatedAt());
    }

    public ProgressResponse toResponse(Progress progress) {
        return new ProgressResponse(
                progress.getId(),
                progress.getEnrollment().getId(),
                progress.getLesson().getId(),
                progress.getCompleted(),
                progress.getScore(),
                progress.getUpdatedAt()
        );
    }
}
