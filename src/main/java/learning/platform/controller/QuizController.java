package learning.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learning.platform.dto.QuizCreateRequest;
import learning.platform.dto.QuizResponse;
import learning.platform.dto.QuizSubmissionRequest;
import learning.platform.dto.QuizSubmissionResponse;
import learning.platform.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@Tag(name = "Quizzes", description = "Gestión de evaluaciones (quiz) asociadas a cursos y lecciones")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @Operation(
            summary = "Crear un nuevo quiz",
            description = "Solo los instructores pueden crear quizzes asociados a cursos o lecciones.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quiz creado",
                            content = @Content(schema = @Schema(implementation = QuizResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(
            @Valid @RequestBody QuizCreateRequest request) {
        QuizResponse response = quizService.createQuiz(request);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Obtener un quiz por ID",
            description = "Devuelve la información del quiz con preguntas y respuestas."
    )
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponse> getQuizById(
            @Parameter(description = "ID del quiz") @PathVariable Long quizId) {
        QuizResponse response = quizService.getQuizById(quizId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener quizzes de un curso",
            description = "Devuelve todos los quizzes asociados a un curso específico."
    )
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<QuizResponse>> getQuizzesByCourse(
            @Parameter(description = "ID del curso") @PathVariable Long courseId) {
        List<QuizResponse> responses = quizService.getQuizzesByCourse(courseId);
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Obtener quiz de una lección",
            description = "Devuelve el quiz asignado a una lección."
    )
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<QuizResponse> getQuizByLesson(
            @Parameter(description = "ID de la lección") @PathVariable Long lessonId) {
        QuizResponse response = quizService.getQuizByLesson(lessonId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Enviar respuestas de un quiz",
            description = "Un estudiante envía sus respuestas a un quiz y recibe puntaje.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Respuestas procesadas",
                            content = @Content(schema = @Schema(implementation = QuizSubmissionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Respuestas inválidas")
            }
    )
    @PostMapping("/{quizId}/submit")
    public ResponseEntity<QuizSubmissionResponse> submitQuiz(
            @Parameter(description = "ID del quiz a resolver") @PathVariable Long quizId,
            @Valid @RequestBody QuizSubmissionRequest request) {
        QuizSubmissionResponse response = quizService.submitQuiz(quizId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar un quiz",
            description = "Solo instructores pueden eliminar quizzes."
    )
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(
            @Parameter(description = "ID del quiz a eliminar") @PathVariable Long quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }
}