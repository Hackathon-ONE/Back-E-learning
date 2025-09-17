package learning.platform.controller;

import jakarta.validation.Valid;
import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.User;
import learning.platform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Devuelve el perfil del usuario logueado, incluyendo IDs de cursos inscriptos
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User authenticatedUser) {
        UserResponse response = userService.getCurrentUser(authenticatedUser);
        return ResponseEntity.ok(response);
    }

    // Obtener usuario por email (solo para ADMIN)
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener usuario por ID (solo para ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // Activar o desactivar usuario (solo para ADMIN)
    @PutMapping("/{id}/active")
    public ResponseEntity<Void> setActive(@PathVariable Long id, @RequestParam boolean active, @AuthenticationPrincipal User user) {
        userService.setActive(id, active, user);
        return ResponseEntity.noContent().build();
    }
}
