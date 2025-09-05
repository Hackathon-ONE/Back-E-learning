CREATE TABLE notification_events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    notification_type VARCHAR(50) NOT NULL CHECK (
        notification_type IN ('NEW_CHAT_MESSAGE', 'NEW_ASSIGNMENT', 'SYSTEM_ALERT', 'GRADE_POSTED')
    ),
    related_id BIGINT,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX IF NOT EXISTS idx_notification_events_type
    ON notification_events(notification_type);

CREATE INDEX IF NOT EXISTS idx_notification_events_created_at
    ON notification_events(created_at);
