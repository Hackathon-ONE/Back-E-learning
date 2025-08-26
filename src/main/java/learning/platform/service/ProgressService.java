import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

public interface ProgressService {
    ProgressResponse markCompleted(Integer enrollmentId, Integer lessonId);
    ProgressResponse updateProgress(Integer enrollmentId, Integer lessonId, ProgressUpdateRequest request);
    Float calculateCourseCompletionPercentage(Integer enrollmentId);
}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressServiceImpl implements ProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private ProgressMapper progressMapper;

    @Override
    @Transactional
    public ProgressResponse markCompleted(Integer enrollmentId, Integer lessonId) {
        // Assuming Enrollment and Lesson entities can be fetched
        Enrollment enrollment = new Enrollment(); // Replace with actual fetching logic
        enrollment.setId(enrollmentId);
        Lesson lesson = new Lesson(); // Replace with actual fetching logic
        lesson.setId(lessonId);

        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson);
        if (progress == null) {
            progress = new Progress();
            progress.setEnrollment(enrollment);
            progress.setLesson(lesson);
        }
        progress.setCompleted(true);
        progress.setUpdatedAt(Instant.now());
        progress = progressRepository.save(progress);
        return progressMapper.toResponse(progress);
    }

    @Override
    @Transactional
    public ProgressResponse updateProgress(Integer enrollmentId, Integer lessonId, ProgressUpdateRequest request) {
        Enrollment enrollment = new Enrollment(); // Replace with actual fetching logic
        enrollment.setId(enrollmentId);
        Lesson lesson = new Lesson(); // Replace with actual fetching logic
        lesson.setId(lessonId);

        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson);
        if (progress == null) {
            progress = new Progress();
            progress.setEnrollment(enrollment);
            progress.setLesson(lesson);
        }
        progressMapper.updateEntityFromRequest(progress, request);
        progress.setUpdatedAt(Instant.now());
        progress = progressRepository.save(progress);
        return progressMapper.toResponse(progress);
    }

    @Override
    @Transactional(readOnly = true)
    public Float calculateCourseCompletionPercentage(Integer enrollmentId) {
        Enrollment enrollment = new Enrollment(); // Replace with actual fetching logic
        enrollment.setId(enrollmentId);
        List<Progress> progresses = progressRepository.findByEnrollment(enrollment);
        if (progresses.isEmpty()) return 0.0f;

        long completedLessons = progresses.stream().filter(Progress::getCompleted).count();
        return (float) (completedLessons * 100) / progresses.size();
    }
}
