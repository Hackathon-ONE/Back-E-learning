package learning.platform.controller;

import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.Course;
import learning.platform.entity.User;
import learning.platform.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    // Aquí inyectarías mappers para convertir entre Entidad y DTO

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Listar cursos (público)
    @GetMapping
    public ResponseEntity<Page<CourseResponseDTO>> getPublicCourses(Pageable pageable) {
        return ResponseEntity.ok(courseService.findAllPublicCourses(pageable));
    }

    // Crear un curso (solo INSTRUCTOR)
    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO dto, @AuthenticationPrincipal User instructor) {
        CourseResponseDTO newCourse = courseService.createCourse(dto, instructor);
        return ResponseEntity.status(201).body(newCourse);
    }

    // Inscribirse a un curso (solo STUDENT)
    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, @AuthenticationPrincipal User student) {
        courseService.enrollStudentInCourse(courseId, student);
        return ResponseEntity.ok("Successfully enrolled.");
    }

    // Aquí irían los endpoints para GET por ID, PUT y DELETE con su @PreAuthorize
}