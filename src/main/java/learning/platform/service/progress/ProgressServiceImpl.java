package learning.platform.service.progress;

import learning.platform.dto.progress.ProgressResponse;
import learning.platform.dto.progress.ProgressUpdateRequest;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.mapper.ProgressMapper;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.LessonRepository;
import learning.platform.repository.ProgressRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final ProgressMapper progressMapper;

    public ProgressServiceImpl(ProgressRepository progressRepository,
                               EnrollmentRepository enrollmentRepository,
                               LessonRepository lessonRepository,
                               ProgressMapper progressMapper) {
        this.progressRepository = progressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.progressMapper = progressMapper;
    }

    @Override
    public ProgressResponse markCompleted(Long enrollmentId, Long lessonId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + enrollmentId));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada: " + lessonId));
        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson);
        if (progress == null) {
            progress = new Progress();
            progress.setEnrollment(enrollment);
            progress.setLesson(lesson);
        }
        progress.setCompleted(true);
        progress.setUpdatedAt(Instant.now());
        return progressMapper.toResponse(progressRepository.save(progress));
    }

    @Override
    public ProgressResponse createOrUpdateProgress(ProgressUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.enrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + request.enrollmentId()));
        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada: " + request.lessonId()));
        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson);
        if (progress == null) {
            progress = progressMapper.toEntity(request); // Asume que el mapper resuelve Enrollment y Lesson
            progress.setEnrollment(enrollment);
            progress.setLesson(lesson);
        } else {
            progressMapper.updateEntityFromRequest(progress, request); // Método no existe, ajustaremos mapper
        }
        progress.setUpdatedAt(Instant.now());
        return progressMapper.toResponse(progressRepository.save(progress));
    }

    @Override
    public Double calculateCourseCompletionPercentage(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + enrollmentId));
        List<Progress> progresses = progressRepository.findByEnrollment(enrollment);
        if (progresses.isEmpty()) return 0.0;
        long completed = progresses.stream().filter(Progress::getCompleted).count();
        // Asumimos que el número total de lecciones viene de LessonRepository
        long totalLessons = lessonRepository.countByCourse(enrollment.getCourse()); // Ajustar según tu modelo
        return (double) (completed * 100) / totalLessons;
    }
}