package learning.platform.service;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;

import java.util.List;

/**
 * Interfaz para el servicio de gestión de lecciones.
 */
public interface LessonService {

    /**
     * Crea una nueva lección a partir de un DTO de solicitud.
     *
     * @param request DTO con los datos de la lección
     * @return DTO de respuesta con los datos de la lección creada
     */
    LessonResponse createLesson(LessonCreateRequest request);

    /**
     * Obtiene una lección por su ID.
     *
     * @param id ID de la lección
     * @return DTO de respuesta con los datos de la lección
     */
    LessonResponse getLessonById(Long id);

    /**
     * Obtiene todas las lecciones asociadas a un curso, ordenadas por orderIndex.
     *
     * @param courseId ID del curso
     * @return Lista de DTOs de respuesta con las lecciones
     */
    List<LessonResponse> getLessonsByCourse(Long courseId);

    /**
     * Actualiza una lección existente.
     *
     * @param id      ID de la lección a actualizar
     * @param request DTO con los nuevos datos de la lección
     * @return DTO de respuesta con los datos de la lección actualizada
     */
    LessonResponse updateLesson(Long id, LessonCreateRequest request);

    /**
     * Elimina una lección por su ID.
     *
     * @param id ID de la lección a eliminar
     */
    void deleteLesson(Long id);

    /**
     * Reordena las lecciones de un curso según una lista de IDs.
     *
     * @param courseId ID del curso
     * @param newOrder Lista de IDs de lecciones en el nuevo orden
     * @return
     */
    List<LessonResponse> reorderLessons(Long courseId, List<Long> newOrder);
}