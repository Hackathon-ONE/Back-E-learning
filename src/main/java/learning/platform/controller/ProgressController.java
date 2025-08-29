package learning.platform.controller;

import learning.platform.dto.ProgressUpdateRequest;
import learning.platform.dto.ProgressResponse;
import learning.platform.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping("/mark-completed")
    public ResponseEntity<ProgressResponse> markCompleted(
            @RequestParam Long enrollmentId,
            @RequestParam Long lessonId) {
        ProgressResponse response = progressService.markCompleted(enrollmentId, lessonId);
        return ResponseEntity.ok(response);
    }

    // Crear o actualizar progreso en una lección:
    @PostMapping
    public ResponseEntity<ProgressResponse> createOrUpdateProgress(
            @RequestBody ProgressUpdateRequest request) {
        ProgressResponse response = progressService.createOrUpdateProgress(request);
        return ResponseEntity.ok(response);
    }

    // Obtener progreso por enrollment:
    @GetMapping("/{enrollmentId}")
    public ResponseEntity<List<ProgressResponse>> getProgressByEnrollment(
            @PathVariable Long enrollmentId) {
        List<ProgressResponse> responses = progressService.getProgressByEnrollment(enrollmentId);
        return ResponseEntity.ok(responses);
    }

    // Calcular porcentaje de completitud del curso:
    @GetMapping("/{enrollmentId}/completion")
    public ResponseEntity<Double> calculateCompletionPercentage(
            @PathVariable Integer enrollmentId) {
        Double percentage = progressService.calculateCourseCompletionPercentage(enrollmentId);
        return ResponseEntity.ok(percentage);
    }
}