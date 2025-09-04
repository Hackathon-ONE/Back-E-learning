CREATE TABLE chat_room_participants (
    chat_room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (chat_room_id, user_id),
    CONSTRAINT fk_chat_room FOREIGN KEY (chat_room_id) REFERENCES chat_rooms(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_chat_room_participants_user ON chat_room_participants(user_id);
CREATE INDEX IF NOT EXISTS idx_chat_room_participants_room ON chat_room_participants(chat_room_id);
