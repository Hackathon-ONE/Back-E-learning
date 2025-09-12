package learning.platform.controller;

import learning.platform.dto.ProgressCourseResponse;
import learning.platform.dto.ProgressUpdateRequest;
import learning.platform.dto.ProgressResponse;
import learning.platform.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('INSTRUCTOR')")
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

    // Actualizar puntaje de una lección para un alumno:
    @PatchMapping("/update-score")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ProgressResponse> updateScore(
            @RequestParam Long enrollmentId,
            @RequestParam Long lessonId,
            @RequestParam Integer score) {
        ProgressResponse response = progressService.updateScore(enrollmentId, lessonId, score);
        return ResponseEntity.ok(response);
    }

    // Actualizar completionPercentage de una lección y ajustar completed automáticamente
    @PatchMapping("/update-percentage")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ProgressResponse> updateCompletionPercentage(
            @RequestParam Long enrollmentId,
            @RequestParam Long lessonId,
            @RequestParam Double completionPercentage) {
        ProgressResponse response = progressService.updateCompletionPercentage(enrollmentId, lessonId, completionPercentage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ProgressCourseResponse> getProgressByCourse(
            @PathVariable Long courseId,
            @RequestParam Long studentId) { // o tomarlo del JWT si es el mismo estudiante.
        ProgressCourseResponse response = progressService.getProgressByCourse(studentId, courseId);
        return ResponseEntity.ok(response);
    }

}