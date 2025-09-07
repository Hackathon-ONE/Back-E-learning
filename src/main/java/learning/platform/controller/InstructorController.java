package learning.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import learning.platform.dto.StudentCourseProgressDTO;
import learning.platform.service.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@Tag(name = "Instructor", description = "Gestión de estudiantes de un instructor")
public class InstructorController {
    private final InstructorService instructorService;
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }
    /**
     * Obtener todos los estudiantes asignados a un instructor
     */
    @Operation(summary = "Lista de estudiantes por instructor", description = "Devuelve los estudiantes que están inscritos en los cursos que imparte un instructor, incluyendo progreso en cada curso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida correctamente"),
            @ApiResponse(responseCode = "204", description = "El instructor no tiene estudiantes"),
            @ApiResponse(responseCode = "404", description = "Instructor no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: solo instructores pueden acceder")
    })

    @GetMapping("/{instructorId}/students")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<StudentCourseProgressDTO>> getStudents(
            @PathVariable @NotNull(message = "El ID del instructor no puede ser nulo") Long instructorId
    ) {
        List<StudentCourseProgressDTO> students = instructorService.getStudentsByInstructor(instructorId);

        if (students.isEmpty()) {
            // Instructor existe pero no tiene estudiantes
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(students);
    }
}