package learning.platform.dto;

import learning.platform.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SystemNotificationRequestDTO {

    @NotNull(message = "El tipo de notificación no puede ser nulo.")
    private NotificationType notificationType;

    @NotBlank(message = "El contenido no puede estar vacío.")
    @Size(min = 10, max = 500, message = "El contenido debe tener entre 10 y 500 caracteres.")
    private String content;

    // --- Constructor Vacío ---
    public SystemNotificationRequestDTO() {
    }

    // --- Nuevo Constructor con argumentos ---
    public SystemNotificationRequestDTO(String content, String notificationType) {
        this.content = content;
        // Convierte el String del rol a un enum NotificationType
        this.notificationType = NotificationType.valueOf("NOTIF_" + notificationType.toUpperCase());
    }


    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}