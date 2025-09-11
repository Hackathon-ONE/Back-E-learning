package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para datos de usuario")
public record UserResponse(
        @Schema(description = "ID único del usuario", example = "1") Long id,
        @Schema(description = "Nombre completo", example = "Ángeles Escudero") String fullName,
        @Schema(description = "Correo electrónico", example = "angeles@example.com") String email,
        @Schema(description = "Rol asignado", example = "ADMIN") String role,
        @Schema(description = "Estado de activación del usuario", example = "true") boolean active,

        String profilePhoto,
        String about
) {}