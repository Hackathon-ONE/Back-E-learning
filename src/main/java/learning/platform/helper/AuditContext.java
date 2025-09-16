package learning.platform.helper;

import org.springframework.stereotype.Component;

@Component
public class AuditContext {
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<String> sessionId = new ThreadLocal<>();

    public void setCurrentUser(String user) {
        currentUser.set(user);
    }

    public String getCurrentUser() {
        return currentUser.get();
    }

    public void clear() {
        currentUser.remove();
        sessionId.remove();
    }

    public void setSessionId(String session) {
        sessionId.set(session);
    }

    public String getSessionId() {
        return sessionId.get();
    }
}


