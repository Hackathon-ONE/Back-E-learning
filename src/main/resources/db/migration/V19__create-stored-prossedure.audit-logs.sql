CREATE OR REPLACE FUNCTION log_table_change()
RETURNS TRIGGER AS $$
DECLARE
    v_user TEXT;
    v_session TEXT;
BEGIN
    -- Obtén al usuario actual (ej: email en aplicación, o current_user si DB user)
    BEGIN
        v_user := current_setting('app.current_user', true);
        v_session := current_setting('app.session_id', true);
    EXCEPTION
        WHEN others THEN
            v_user := NULL;
            v_session := NULL;
    END;

    IF (TG_OP = 'DELETE') THEN
        INSERT INTO audit_logs(table_name, change_type, old_data, change_by, session_id)
        VALUES (TG_TABLE_NAME, 'DELETE', to_jsonb(OLD), v_user, v_session);

        RETURN OLD;

    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO audit_logs(table_name, change_type, old_data, new_data, change_by, session_id)
        VALUES (TG_TABLE_NAME, 'UPDATE', to_jsonb(OLD), to_jsonb(NEW), v_user, v_session);

        RETURN NEW;

    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO audit_logs(table_name, change_type, new_data, change_by, session_id)
        VALUES (TG_TABLE_NAME, 'INSERT', to_jsonb(NEW), v_user, v_session);

        RETURN NEW;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


-- Crear trigger en users
DROP TRIGGER IF EXISTS users_audit_trigger ON users;
CREATE TRIGGER users_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON users
FOR EACH ROW
EXECUTE FUNCTION log_table_change();
