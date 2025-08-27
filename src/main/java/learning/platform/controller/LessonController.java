package learning.platform.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.dto.lesson.LessonResponse;
import learning.platform.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lessons")
@SecurityRequirement(name = "bearer-key")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')") // Solo los instructores pueden crear lecciones.
    public ResponseEntity<LessonResponse> createLesson(
            @RequestBody @Validated LessonCreateRequest request,
            UriComponentsBuilder uriComponentsBuilder) {
        LessonResponse lessonResponse = lessonService.createLesson(request);
        URI url = uriComponentsBuilder.path("/lessons/{id}")
                .buildAndExpand(lessonResponse.getId()).toUri();
        return ResponseEntity.created(url).body(lessonResponse);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')") // Todos pueden listar lecciones de un curso.
    public ResponseEntity<List<LessonResponse>> getLessonsByCourse(@PathVariable Integer courseId) {
        List<LessonResponse> lessons = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(lessons);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')") // Solo los instructores pueden actualizar lecciones.
    public ResponseEntity<LessonResponse> updateLesson(
            @PathVariable Integer id,
            @RequestBody @Validated LessonCreateRequest request) {
        LessonResponse lessonResponse = lessonService.updateLesson(id, request);
        return ResponseEntity.ok(lessonResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')") // Solo los instructores pueden eliminar lecciones.
    public ResponseEntity<Void> deleteLesson(@PathVariable Integer id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reorder/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')") // Solo los instructores pueden reordenar lecciones.
    public ResponseEntity<Void> reorderLessons(
            @PathVariable Integer courseId,
            @RequestBody List<Integer> newOrder) {
        lessonService.reorderLessons(courseId, newOrder);
        return ResponseEntity.ok().build();
    }
}
