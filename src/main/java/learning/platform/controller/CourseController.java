package learning.platform.controller;

import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.User;
import learning.platform.repository.UserRepository;
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

    private final CourseService  courseService;
    private final UserRepository userRepository;

    //Inicializamos
    public CourseController(CourseService courseService, UserRepository userRepository) {
        this.courseService = courseService;
        this.userRepository = userRepository;
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

        //Simula el instructor obteniéndolo de la BD
        // Asegúrate de tener un usuario con ID=1 y rol INSTRUCTOR en tu base de datos
        /*User instructor = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Usuario instructor de prueba no encontrado"));
         */


        CourseResponseDTO newCourse = courseService.createCourse(dto, instructor);
        return ResponseEntity.status(201).body(newCourse);
    }

    // Inscribirse a un curso (solo STUDENT)
    /*
    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, @AuthenticationPrincipal User student) {
        courseServiceImpl.enrollStudentInCourse(courseId, student);
        return ResponseEntity.ok("Successfully enrolled.");
    }

     */

    /**
     * Obtener curso por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findCourseDtoById(id));
    }

    /**
     * Actualizar el curso (Solamente puede el instructor)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseRequestDTO dto) {
        return ResponseEntity.ok(courseService.updateCourse(id, dto));
    }

    /**
     * Delete a course (INSTRUCTOR or ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build(); // Returns a 204 No Content status
    }
}