package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de respuesta para datos de usuario")
public record UserResponse(
        @Schema(description = "ID único del usuario", example = "1") Long id,
        @Schema(description = "Nombre completo", example = "Ángeles Escudero") String fullName,
        @Schema(description = "Correo electrónico", example = "angeles@example.com") String email,
        @Schema(description = "Rol asignado", example = "ADMIN") String role,
        @Schema(description = "Estado de activación del usuario", example = "true") boolean active,
        @Schema(description = "URL de la foto de perfil", example = "https://example.com/photo.jpg") String profilePhoto,
        @Schema(description = "Descripción personal del usuario", example = "Apasionada por la automatización y el backend") String about,
        @Schema(description = "IDs de cursos en los que el usuario está inscripto", example = "[4, 7, 12]") List<Long> enrolledCourses
) {}