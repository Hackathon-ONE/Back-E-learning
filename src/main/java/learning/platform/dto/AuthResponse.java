package learning.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación con token JWT")
public record AuthResponse(
        @Schema(description = "Token JWT generado para el usuario autenticado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Rol del usuario autenticado", example = "STUDENT")
        String role
) {}
