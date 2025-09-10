package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO de respuesta para datos del usuario logueado")
public record UserResponse(
        @Schema(description = "ID único del usuario", example = "3") Long id,
        @Schema(description = "Nombre del usuario", example = "Angeles") String fullName,
        @Schema(description = "Correo electrónico", example = "angeles@lumina.com") String email,
        @Schema(description = "Roles asignados", example = "[\"STUDENT\"]") List<String> roles,
        @Schema(description = "IDs de cursos en los que está inscripto", example = "[1, 2, 5]") List<Long> enrolledCourses
) {}