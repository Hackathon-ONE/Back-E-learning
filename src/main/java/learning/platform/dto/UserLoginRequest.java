package learning.platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciales para login de usuario")
public record UserLoginRequest(
        @Schema(description = "Correo electrónico del usuario", example = "angeles@example.com")
        @Email @NotBlank String email,

        @Schema(description = "Contraseña del usuario", example = "segura123")
        @NotBlank String password
) {}

