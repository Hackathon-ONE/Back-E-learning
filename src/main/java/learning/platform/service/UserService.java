package learning.platform.service;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponse register(UserRegisterRequest request);

    Optional<UserResponse> findByEmail(String email);

    UserResponse getById(Long id);

    void setActive(Long id, boolean active);

    /**
     * Mapea la entidad User autenticada al DTO de respuesta.
     * Usado en el endpoint /api/users/me para devolver datos del usuario logueado.
     */
    UserResponse mapToUserResponse(User user);

    /**
     * ✅ Nuevo método para obtener el usuario con cursos inscriptos.
     * Evita LazyInitializationException usando JOIN FETCH.
     */
    UserResponse getCurrentUser(String email);

    /**
     * (Opcional) Método para inscribir cursos si lo necesitás.
     */
    void enrollUserInCourses(Long userId, List<Long> courseIds);
}
