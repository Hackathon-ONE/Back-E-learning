package learning.platform.controller;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;
import learning.platform.entity.User;
import learning.platform.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.format.DateTimeParseException;
import java.util.List;


@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    // Crear una lección (sólo un instructor):
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<LessonResponse> createLesson(@Valid @RequestBody LessonCreateRequest request, @AuthenticationPrincipal User user) {
        LessonResponse response = lessonService.createLesson(request, user);
        return ResponseEntity.status(201).body(response);
    }

    // Obtener lecciones por curso (todos):
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonResponse>> getLessonsByCourse(@PathVariable Long courseId) {
        List<LessonResponse> responses = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(responses);
    }

    // Editar una lección (sólo instructor):
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long lessonId, @Valid @RequestBody LessonCreateRequest request, @AuthenticationPrincipal User user) {
        LessonResponse response = lessonService.updateLesson(lessonId, request,user);
        return ResponseEntity.ok(response);
    }

    // Borrar una lección (sólo instructor):
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId, @AuthenticationPrincipal User user) {
        lessonService.deleteLesson(lessonId, user);
        return ResponseEntity.noContent().build();
    }

    // Reordenar lecciones (sólo instructor):
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{courseId}/reorder")
    public ResponseEntity<List<LessonResponse>> reorderLessons(@PathVariable Long courseId, @RequestBody List<Long> newOrder) {
        List<LessonResponse> responses = lessonService.reorderLessons(courseId, newOrder);
        return ResponseEntity.ok(responses);
    }

    // Manejo de excepciones:
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity.status(400).body("Error en el formato de duration: " + ex.getMessage() + ". Use formato ISO 8601 (ej. PT30M)");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}