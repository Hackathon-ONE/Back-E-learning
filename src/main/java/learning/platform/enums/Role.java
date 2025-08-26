package learning.platform.enums;

public enum Role {
    STUDENT("Estudiante"),
    INSTRUCTOR("Instructor"),
    ADMIN("Administrador");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
