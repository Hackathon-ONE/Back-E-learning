package learning.platform.helper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class AuditPropagator {

    @PersistenceContext
    private EntityManager entityManager;

    private final AuditContext auditContext;

    public AuditPropagator(AuditContext auditContext) {
        this.auditContext = auditContext;
    }

    /**
     * Ejecuta SET LOCAL usando set_config para que el trigger pueda leer current_setting(...)
     * Debe llamarse dentro de la misma transacción que las operaciones que queremos auditar.
     */
    public void propagate() {
        String user = auditContext.getCurrentUser();
        String session = auditContext.getSessionId();

        if (user != null) {
            // usa set_config con parámetro; evita problemas de parsing
            entityManager.createNativeQuery("SELECT set_config('app.current_user', :user, true)")
                    .setParameter("user", user)
                    .getSingleResult();
        }

        if (session != null) {
            entityManager.createNativeQuery("SELECT set_config('app.session_id', :session, true)")
                    .setParameter("session", session)
                    .getSingleResult();
        }

        // opcional: limpiar el ThreadLocal al finalizar la tx para evitar fugas
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    auditContext.clear();
                }
            });
        }
    }
}
