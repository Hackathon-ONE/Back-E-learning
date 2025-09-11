package learning.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.User;
import learning.platform.repository.UserRepository;
import learning.platform.service.impl.CourseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@PreAuthorize("hasRole('ADMIN')")
@Validated
@Tag(name = "Admin - Cursos", description = "Gestión de cursos por administradores")
public class AdminCourseController {

    private final CourseServiceImpl courseService;
    private final UserRepository userRepository;

    public AdminCourseController(CourseServiceImpl courseService, UserRepository userRepository) {
        this.courseService = courseService;
        this.userRepository = userRepository;
    }

    // Listar todos los cursos con paginación
    @Operation(summary = "Listar todos los cursos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos listados correctamente")
    })
    @GetMapping
    public ResponseEntity<Page<CourseResponseDTO>> getAllCourses(Pageable pageable) {
        Page<CourseResponseDTO> courses = courseService.findAllPublicCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    // Obtener un curso por ID
    @Operation(summary = "Obtener un curso por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        CourseResponseDTO course = courseService.findCourseDtoById(id);
        return ResponseEntity.ok(course);
    }

    // Crear curso como Admin (con instructor por defecto)
    @Operation(summary = "Crear un nuevo curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso creado correctamente"),
            @ApiResponse(responseCode = "404", description = "Instructor por defecto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de curso inválidos")
    })
    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO dto) {
        User instructor = userRepository.findByEmail("instructor@lumina.com")
                .orElseThrow(() -> new RuntimeException("Instructor por defecto no encontrado"));
        CourseResponseDTO course = courseService.createCourse(dto, instructor);
        return ResponseEntity.ok(course);
    }

    // Actualizar curso
    @Operation(summary = "Actualizar un curso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de curso inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequestDTO dto
    ) {
        CourseResponseDTO updatedCourse = courseService.updateCourse(id, dto);
        return ResponseEntity.ok(updatedCourse);
    }

    // Eliminar curso
    @Operation(summary = "Eliminar un curso por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
