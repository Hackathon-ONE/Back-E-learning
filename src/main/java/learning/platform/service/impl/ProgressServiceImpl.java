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

import java.math.BigDecimal;
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

        progress.setCompletionPercentage(BigDecimal.valueOf(100.0));
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

        // Actualizamos score:
        progress.setScore(request.score());

        // Actualizamos completionPercentage y completed:
        if (request.completionPercentage() != null) {
            progress.setCompletionPercentage(request.completionPercentage());
            progress.setCompleted(request.completionPercentage().compareTo(new BigDecimal("100.0")) >= 0);
        }

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

        // Promedio de completionPercentage
        double totalPercentage = progressList.stream()
                .mapToDouble(p -> p.getCompletionPercentage() != null ? p.getCompletionPercentage().doubleValue() : 0.0)
                .sum();

        return totalPercentage / progressList.size();
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

        // Marcamos completed si completionPercentage ya es 100:
        if (progress.getCompletionPercentage() != null
                && progress.getCompletionPercentage().compareTo(new BigDecimal("100.0")) >= 0) {
            progress.setCompleted(true);
        }


        progress.setUpdatedAt(Instant.now());
        progressRepository.save(progress);

        return progressMapper.toResponse(progress);
    }

    @Override
    public ProgressResponse updateCompletionPercentage(Long enrollmentId, Long lessonId, Double completionPercentage) {
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

        // Guardamos completionPercentage como BigDecimal:
        progress.setCompletionPercentage(completionPercentage != null ? BigDecimal.valueOf(completionPercentage) : BigDecimal.ZERO);

        // Si llega a 100%, marcamos completed
        if (completionPercentage != null && completionPercentage >= 100.0) {
            progress.setCompleted(true);
        }

        progress.setUpdatedAt(Instant.now());
        progressRepository.save(progress);

        return progressMapper.toResponse(progress);
    }

}
