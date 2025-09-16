CREATE TABLE audit_logs(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    table_name TEXT NOT NULL,
    change_type TEXT  NOT NULL,
    old_data JSONB,
    new_data JSONB,
    change_by TEXT,
    session_id TEXT,
    change_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
