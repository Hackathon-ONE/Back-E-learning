package learning.platform.controller;

import learning.platform.dto.MaterialCreateRequest;
import learning.platform.dto.MaterialResponse;
import learning.platform.dto.MaterialUpdateRequest;
import learning.platform.service.MaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    // Crear un material (solo instructor):
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/lessons/{lessonId}/materials")
    public ResponseEntity<MaterialResponse> createMaterial(
            @PathVariable Long lessonId,
            @Valid @RequestBody MaterialCreateRequest request) {
        MaterialResponse response = materialService.createMaterial(lessonId, request);
        return ResponseEntity.status(201).body(response);
    }

    // Obtener todos los materiales de una lección (control BE-011)
    @GetMapping("/lessons/{lessonId}/materials")
    public ResponseEntity<List<MaterialResponse>> getMaterialsByLesson(
            @PathVariable Long lessonId,
            @AuthenticationPrincipal(expression = "id") Long userId) {
        List<MaterialResponse> responses = materialService.getMaterialsByLesson(lessonId, userId);
        return ResponseEntity.ok(responses);
    }

    // Obtener un material por ID (control BE-011)
    @GetMapping("/materials/{materialId}")
    public ResponseEntity<MaterialResponse> getMaterialById(
            @PathVariable Long materialId,
            @AuthenticationPrincipal(expression = "id") Long userId) {
        MaterialResponse response = materialService.getMaterialById(materialId, userId);
        return ResponseEntity.ok(response);
    }

    // Actualizar un material (solo instructor)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/materials/{materialId}")
    public ResponseEntity<MaterialResponse> updateMaterial(
            @PathVariable Long materialId,
            @Valid @RequestBody MaterialUpdateRequest request) {
        MaterialResponse response = materialService.updateMaterial(materialId, request);
        return ResponseEntity.ok(response);
    }

    // Eliminar un material (solo instructor)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/materials/{materialId}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long materialId) {
        materialService.deleteMaterial(materialId);
        return ResponseEntity.noContent().build();
    }

    // Manejo de excepciones
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}
