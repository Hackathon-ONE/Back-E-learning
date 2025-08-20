CREATE TABLE progress(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    enrollment_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    completion_percentage NUMERIC (5,2) DEFAULT 0.00,

    CONSTRAINT fk_enrollment
        FOREIGN KEY (enrollment_id) REFERENCES enrollments(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lesson
        FOREIGN KEY (lesson_id) REFERENCES lessons(id)
        ON DELETE CASCADE
);