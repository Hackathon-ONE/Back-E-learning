package learning.platform.controller;

import learning.platform.dto.SystemNotificationRequestDTO;
import learning.platform.dto.NotificationResponseDTO;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import learning.platform.repository.UserRepository;
import learning.platform.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
//@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository; // <-- 1. Inyecta el repositorio

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository; // <-- 2. Inicialízalo
    }

    // Nuevo DTO para que el controlador reciba el rol de destino
    public record TargetedNotificationRequestDTO(String content, String role) {}

    // Este es el endpoint que manejará el envío con lógica de roles
    @PostMapping("/send")
    // @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Void> sendTargetedNotification(@RequestBody TargetedNotificationRequestDTO request) {
        // SIMULACIÓN: En un entorno real, @AuthenticationPrincipal te daría el usuario
        // User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Usuario de prueba no encontrado"));
        Role senderRole = currentUser.getRole();

        // Lógica de negocio para el envío de notificaciones por rol
        switch (senderRole) {
            case ADMIN:
                // ADMIN puede enviar a cualquier rol
                notificationService.createSystemNotification(
                        new SystemNotificationRequestDTO(request.content(), request.role())
                );
                break;
            case INSTRUCTOR:
                // INSTRUCTOR solo puede enviar a estudiantes o a sí mismo (para el sistema)
                if (request.role().equals("STUDENT")) {
                    notificationService.createSystemNotification(
                            new SystemNotificationRequestDTO(request.content(), request.role())
                    );
                } else {
                    return ResponseEntity.status(403).build(); // Forbidden
                }
                break;
            default:
                return ResponseEntity.status(403).build(); // Forbidden
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/system")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> sendSystemNotification(@Valid @RequestBody SystemNotificationRequestDTO request) {
        notificationService.createSystemNotification(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Page<NotificationResponseDTO>> getMyNotifications(
            /*@AuthenticationPrincipal User user,*/ Pageable pageable) {
        // 5. Simula un usuario para la prueba (asegúrate de que el usuario con ID 1 exista)
        User testUser = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Usuario de prueba no encontrado"));

        Page<NotificationResponseDTO> notifications = notificationService.getUnreadNotificationsForUser(testUser.getId(), pageable);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{eventId}/read")
    public ResponseEntity<Void> markAsRead(
            /*@AuthenticationPrincipal User user,*/ @PathVariable Long eventId) {
        // 7. Simula el mismo usuario para la prueba
        User testUser = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Usuario de prueba no encontrado"));


        notificationService.markNotificationAsRead(testUser.getId(), eventId);
        return ResponseEntity.ok().build();
    }
}