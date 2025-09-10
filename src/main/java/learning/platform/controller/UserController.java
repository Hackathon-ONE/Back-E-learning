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

    // ✅ Corregido: usa el método que carga cursos con JOIN FETCH
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        UserResponse response = userService.getCurrentUser(user.getEmail());
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
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok(response);
    }

    // Activar o desactivar usuario (solo para ADMIN)
    @PutMapping("/{id}/active")
    public ResponseEntity<Void> setActive(@PathVariable Long id, @RequestParam boolean active) {
        userService.setActive(id, active);
        return ResponseEntity.noContent().build();
    }
}
