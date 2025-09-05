CREATE TABLE chat_rooms (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100),
    room_type VARCHAR(20) NOT NULL CHECK (room_type IN ('DIRECT_MESSAGE', 'COURSE_GROUP')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_chat_rooms_room_type ON chat_rooms(room_type);
