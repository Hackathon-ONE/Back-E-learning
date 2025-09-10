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
 * Implementación del servicio de materiales.
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
    public List<MaterialResponse> getMaterialsByLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con ID: " + lessonId));

        return materialRepository.findByLesson(lesson)
                .stream()
                .map(materialMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMaterial(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new IllegalArgumentException("Material no encontrado con ID: " + materialId);
        }
        materialRepository.deleteById(materialId);
    }

    @Override
    public MaterialResponse getMaterialById(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado con ID: " + materialId));
        return materialMapper.toResponse(material);
    }

    @Override
    public MaterialResponse updateMaterial(Long materialId, MaterialUpdateRequest request) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado con ID: " + materialId));

        material.setTitle(request.title());
        material.setContentUrl(request.contentUrl());
        material.setContentType(request.contentType());

        Material updated = materialRepository.save(material);
        return materialMapper.toResponse(updated);
    }
}
