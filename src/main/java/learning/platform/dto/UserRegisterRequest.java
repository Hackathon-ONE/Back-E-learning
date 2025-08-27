package learning.platform.dto;

import jakarta.validation.constraints.*;

public record UserRegisterRequest(
        @NotBlank String fullName,
        @Email String email,
        @Size(min = 8) String password,
        @Pattern(regexp = "STUDENT|INSTRUCTOR|ADMIN", message = "Rol inválido") String role
) {}