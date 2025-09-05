package learning.platform.enums;

public enum NotificationType {
    NEW_CHAT_MESSAGE,
    NEW_ASSIGNMENT,
    SYSTEM_ALERT,
    GRADE_POSTED,
    NOTIF_GENERAL,      // Para todos (STUDENT e INSTRUCTOR)
    NOTIF_STUDENT,     // Solo para STUDENT
    NOTIF_INSTRUCTOR;  // Solo para INSTRUCTOR
}
