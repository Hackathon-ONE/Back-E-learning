CREATE TABLE courses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(25) NOT NULL,
    instructor_id BIGINT NOT NULL,

    CONSTRAINT fk_instructor
        FOREIGN KEY (instructor_id) REFERENCES users(id)
);
