package learning.platform.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NotificationRecipientId implements Serializable {
    private Long eventId;
    private Long userId;

    // Constructor vacío requerido por JPA
    public NotificationRecipientId() {}

    // Constructor con argumentos
    public NotificationRecipientId(Long eventId, Long userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    // Getters y Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationRecipientId that = (NotificationRecipientId) o;
        return Objects.equals(eventId, that.eventId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId);
    }
}