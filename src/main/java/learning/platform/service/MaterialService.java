package learning.platform.service;

import learning.platform.dto.MaterialCreateRequest;
import learning.platform.dto.MaterialResponse;
import learning.platform.dto.MaterialUpdateRequest;

import java.util.List;

/**
 * Interfaz para la gestión de materiales de lecciones.
 */
public interface MaterialService {

    /**
     * Crea un nuevo material dentro de una lección.
     *
     * @param lessonId ID de la lección a la que se asocia el material
     * @param request DTO con los datos del material
     * @return DTO de respuesta con los datos del material creado
     */
    MaterialResponse createMaterial(Long lessonId, MaterialCreateRequest request);

    /**
     * Obtiene todos los materiales de una lección.
     *
     * @param lessonId ID de la lección
     * @return lista de DTOs con los materiales
     */
    List<MaterialResponse> getMaterialsByLesson(Long lessonId);

    /**
     * Elimina un material por su ID.
     *
     * @param materialId ID del material
     */
    void deleteMaterial(Long materialId);

    MaterialResponse getMaterialById(Long materialId);
    MaterialResponse updateMaterial(Long materialId, MaterialUpdateRequest request);

}
