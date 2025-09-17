package learning.platform.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_recipients")
public class NotificationRecipient {

    @EmbeddedId
    private NotificationRecipientId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private NotificationEvent event;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isRead = false;

    // Constructor vacío
    public NotificationRecipient() {}

    // Getters y Setters
    public NotificationRecipientId getId() { return id; }
    public void setId(NotificationRecipientId id) { this.id = id; }

    public NotificationEvent getEvent() { return event; }
    public void setEvent(NotificationEvent event) { this.event = event; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}