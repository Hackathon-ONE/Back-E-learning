package learning.platform.service.impl;

import learning.platform.dto.MaterialCreateRequest;
import learning.platform.dto.MaterialResponse;
import learning.platform.dto.MaterialUpdateRequest;
import learning.platform.entity.Lesson;
import learning.platform.entity.Material;
import learning.platform.mapper.MaterialMapper;
import learning.platform.repository.LessonRepository;
import learning.platform.repository.MaterialRepository;
import learning.platform.service.MaterialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de materiales con control de acceso:
 */
@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final LessonRepository lessonRepository;
    private final MaterialMapper materialMapper;

    public MaterialServiceImpl(MaterialRepository materialRepository,
                               LessonRepository lessonRepository,
                               MaterialMapper materialMapper) {
        this.materialRepository = materialRepository;
        this.lessonRepository = lessonRepository;
        this.materialMapper = materialMapper;
    }

    // -------------------- CRUD BÁSICO --------------------

    @Override
    public MaterialResponse createMaterial(Long lessonId, MaterialCreateRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con ID: " + lessonId));

        Material material = materialMapper.toEntity(request);
        material.setLesson(lesson);

        Material saved = materialRepository.save(material);
        return materialMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public MaterialResponse updateMaterial(Long materialId, MaterialUpdateRequest request) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado con ID: " + materialId));

        // MapStruct actualiza los campos title, contentUrl y contentType
        materialMapper.updateFromDto(request, material);

        Material updated = materialRepository.save(material);
        return materialMapper.toResponse(updated);
    }

    @Override
    public void deleteMaterial(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new IllegalArgumentException("Material no encontrado con ID: " + materialId);
        }
        materialRepository.deleteById(materialId);
    }

    // -------------------- CONTROL DE ACCESO --------------------

    @Override
    public MaterialResponse getMaterialById(Long materialId, Long userId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado con ID: " + materialId));

        boolean isAuthorized = checkUserAccess(material.getLesson().getCourse().getId(), userId);
        MaterialResponse response = materialMapper.toResponse(material);

        if (!isAuthorized) {
            // Ocultar URL de contenido si no está autorizado
            response = new MaterialResponse(
                    response.id(),
                    response.lessonId(),
                    response.title(),
                    null, // contentUrl oculto
                    response.contentType()
            );
        }

        return response;
    }

    @Override
    public List<MaterialResponse> getMaterialsByLesson(Long lessonId, Long userId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con ID: " + lessonId));

        boolean isAuthorized = checkUserAccess(lesson.getCourse().getId(), userId);

        return materialRepository.findByLesson(lesson)
                .stream()
                .map(materialMapper::toResponse)
                .map(resp -> {
                    if (!isAuthorized) {
                        return new MaterialResponse(
                                resp.id(),
                                resp.lessonId(),
                                resp.title(),
                                null, // contentUrl oculto
                                resp.contentType()
                        );
                    }
                    return resp;
                })
                .collect(Collectors.toList());
    }

    // -------------------- MÉTODO AUXILIAR --------------------

    /**
     * Decide si un usuario puede acceder al contenido completo de un material.
     * Implementar según:
     * - Inscripción en el curso
     * - Instructor del curso
     * - Rol admin
     */
    private boolean checkUserAccess(Long courseId, Long userId) {
        // TODO: lógica real con repositorios/roles
        return true; // Por ahora siempre autorizado
    }
}
