package learning.platform.service.impl;

import learning.platform.dto.ProgressUpdateRequest;
import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.mapper.ProgressMapper;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.LessonRepository;
import learning.platform.repository.ProgressRepository;
import learning.platform.service.ProgressService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final ProgressMapper progressMapper;

    public ProgressServiceImpl(
            ProgressRepository progressRepository,
            EnrollmentRepository enrollmentRepository,
            LessonRepository lessonRepository,
            ProgressMapper progressMapper
    ) {
        this.progressRepository = progressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.progressMapper = progressMapper;
    }

    @Override
    public ProgressResponse markCompleted(Long enrollmentId, Long lessonId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment no encontrado: " + enrollmentId));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson no encontrada: " + lessonId));

        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .orElseGet(() -> {
                    Progress newProgress = new Progress();
                    newProgress.setEnrollment(enrollment);
                    newProgress.setLesson(lesson);
                    return newProgress;
                });

        progress.setCompleted(true);
        progress.setUpdatedAt(Instant.now());

        progressRepository.save(progress);
        return progressMapper.toResponse(progress);
    }

    @Override
    public ProgressResponse getProgress(Long enrollmentId, Long lessonId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment no encontrado: " + enrollmentId));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson no encontrada: " + lessonId));

        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .orElseThrow(() -> new IllegalArgumentException("Progress no encontrado para enrollment y lesson dados"));

        return progressMapper.toResponse(progress);
    }

    @Override
    public List<ProgressResponse> getAllProgressByEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment no encontrado: " + enrollmentId));

        return progressRepository.findByEnrollment(enrollment)
                .stream()
                .map(progressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProgressResponse createOrUpdateProgress(ProgressUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.enrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Enrollment no encontrado: " + request.enrollmentId()));
        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson no encontrada: " + request.lessonId()));

        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .orElseGet(() -> {
                    Progress newProgress = new Progress();
                    newProgress.setEnrollment(enrollment);
                    newProgress.setLesson(lesson);
                    return newProgress;
                });

        progress.setCompleted(request.completed());
        progress.setScore(request.score());
        progress.setUpdatedAt(request.updatedAt() != null ? request.updatedAt() : Instant.now());

        progressRepository.save(progress);
        return progressMapper.toResponse(progress);
    }

    @Override
    public List<ProgressResponse> getProgressByEnrollment(Long enrollmentId) {
        return getAllProgressByEnrollment(enrollmentId);
    }

    @Override
    public Double calculateCourseCompletionPercentage(Integer enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(Long.valueOf(enrollmentId))
                .orElseThrow(() -> new IllegalArgumentException("Enrollment no encontrado: " + enrollmentId));

        List<Progress> progressList = progressRepository.findByEnrollment(enrollment);

        if (progressList.isEmpty()) return 0.0;

        long completedCount = progressList.stream().filter(Progress::getCompleted).count();
        return (completedCount * 100.0) / progressList.size();
    }

    @Override
    public ProgressResponse updateScore(Long enrollmentId, Long lessonId, Integer score) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment no encontrado: " + enrollmentId));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson no encontrada: " + lessonId));

        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Progress no encontrado para enrollmentId=" + enrollmentId + " y lessonId=" + lessonId));

        progress.setScore(score);
        progress.setUpdatedAt(Instant.now());

        progressRepository.save(progress);
        return progressMapper.toResponse(progress);
    }

}
