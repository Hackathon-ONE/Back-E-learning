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

    /**
     * Actualiza el puntaje (score) de una lección dentro de una inscripción.
     *
     * @param enrollmentId ID de la inscripción
     * @param lessonId     ID de la lección
     * @param score        Puntaje a asignar
     * @return Progreso actualizado
     */
    ProgressResponse updateScore(Long enrollmentId, Long lessonId, Integer score);

    ProgressResponse updateCompletionPercentage(Long enrollmentId, Long lessonId, Double completionPercentage);
}
