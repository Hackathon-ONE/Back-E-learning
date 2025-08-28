package learning.platform.controller;

import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.dto.lesson.LessonResponse;
import learning.platform.service.lesson.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<LessonResponse> createLesson(@RequestBody LessonCreateRequest request) {
        LessonResponse response = lessonService.createLesson(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonResponse>> getLessonsByCourse(@PathVariable Long courseId) {
        List<LessonResponse> responses = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long lessonId, @RequestBody LessonCreateRequest request) {
        LessonResponse response = lessonService.updateLesson(lessonId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/reorder")
    public ResponseEntity<List<LessonResponse>> reorderLessons(@PathVariable Long courseId, @RequestBody List<Long> newOrder) {
        List<LessonResponse> responses = lessonService.reorderLessons(courseId, newOrder);
        return ResponseEntity.ok(responses);
    }
}