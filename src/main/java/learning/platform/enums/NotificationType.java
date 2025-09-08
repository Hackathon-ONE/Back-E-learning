package learning.platform.enums;

public enum NotificationType {
    NEW_CHAT_MESSAGE,
    NEW_ASSIGNMENT,
    SYSTEM_ALERT,
    GRADE_POSTED,
    NOTIF_GENERAL,      // Para todos (STUDENT e INSTRUCTOR)
    NOTIF_STUDENTS,     // Solo para STUDENT
    NOTIF_INSTRUCTORS;  // Solo para INSTRUCTOR
}
