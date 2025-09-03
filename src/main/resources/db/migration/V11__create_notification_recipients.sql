CREATE TABLE notification_recipients (
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (event_id, user_id),

    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES notification_events(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_recipient FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_notification_recipients_user
    ON notification_recipients(user_id);

CREATE INDEX IF NOT EXISTS idx_notification_recipients_event
    ON notification_recipients(event_id);
