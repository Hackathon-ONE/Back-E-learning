package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "ID único del usuario") Long id,
        @Schema(description = "Nombre completo") String fullName,
        @Schema(description = "Correo electrónico") String email,
        @Schema(description = "Rol asignado") String role,
        @Schema(description = "Si el usuario está activo") boolean active
) {}
