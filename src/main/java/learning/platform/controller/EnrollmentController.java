package learning.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learning.platform.config.TokenService;
import learning.platform.dto.EnrollmentRequest;
import learning.platform.dto.EnrollmentResponse;
import learning.platform.entity.User;
import learning.platform.service.impl.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollment")
@Tag(name = "Enrollments", description = "Operaciones para gestionar inscripciones de estudiantes a cursos")
public class EnrollmentController {

    private final EnrollmentServiceImpl enrollmentService;

    public EnrollmentController(EnrollmentServiceImpl enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Operation(summary = "Crear inscripción", description = "Inscribe a un estudiante en un curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    })
    @PostMapping("/create")
    public ResponseEntity<EnrollmentResponse> createEnrollment(@Valid @RequestBody EnrollmentRequest datos){
        var enrollment = enrollmentService.enrollStudent(datos);
        return ResponseEntity.ok(enrollment);
    }

    @Operation(summary = "Completar inscripción", description = "Marca un curso como completado para el estudiante")
    @PutMapping("/complete/{enrollmentId}")
    public ResponseEntity<EnrollmentResponse> endEnrollment(@PathVariable Long enrollmentId){
        var erollment = enrollmentService.completeCourse(enrollmentId);
        return  ResponseEntity.ok(erollment);
    }

    @Operation(summary = "Cancelar inscripción", description = "Cancela una inscripción existente")
    @PutMapping("/cancel/{enrollmentId}")
    public ResponseEntity<EnrollmentResponse> cancellEnrollment(@PathVariable Long enrollmentId){
        var erollment = enrollmentService.cancelCourse(enrollmentId);
        return  ResponseEntity.ok(erollment);
    }

    @Operation(summary = "Mis inscripciones", description = "Obtiene todas las inscripciones del estudiante autenticado")
    @GetMapping("/my-enrollments")
    public ResponseEntity<Page<EnrollmentResponse>> allMyEnrolls(@AuthenticationPrincipal User user, Pageable pageable){
        return ResponseEntity.ok(enrollmentService.findByStudent(user.getId(), pageable));
    }

    @Operation(summary = "Inscripciones de un curso", description = "Obtiene todas las inscripciones de un curso, accesible para instructores")
    @GetMapping("/course-enrollments")
    public ResponseEntity<Page<EnrollmentResponse>> courseEnrolls(@AuthenticationPrincipal User user, Pageable pageable){
        return ResponseEntity.ok(enrollmentService.findByCourse(user.getId(), pageable));
    }


    @Operation(summary = "Eliminar inscripción", description = "Elimina una inscripción específica")
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity delateEnrollment(@Parameter(description = "ID de la inscripción a eliminar") @PathVariable Long enrollmentId){
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

}
