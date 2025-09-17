CREATE TABLE enrollments(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
    progress_percent INT DEFAULT 0,

    CONSTRAINT fk_student
        FOREIGN KEY (student_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_student_course UNIQUE (student_id, course_id)
);