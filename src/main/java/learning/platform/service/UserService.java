package learning.platform.service;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.User;

import java.util.Optional;

public interface UserService {

    /**
     * Registra un nuevo usuario en la plataforma.
     */
    UserResponse register(UserRegisterRequest request);

    /**
     * Busca un usuario por email.
     */
    Optional<UserResponse> findByEmail(String email);

    /**
     * Obtiene un usuario por su ID.
     */
    UserResponse getById(Long id);

    void setActive(Long id, boolean active, User user);

    /**
     * ✅ Método para obtener los datos del usuario logueado desde el token JWT.
     * Usado en el endpoint /api/users/me con @AuthenticationPrincipal.
     */
    UserResponse getCurrentUser(User user);
}