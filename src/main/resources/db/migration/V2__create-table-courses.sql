CREATE TABLE courses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    slug VARCHAR(100) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(25) NOT NULL,
    instructor_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL,
    is_open BOOLEAN NOT NULL,
    published BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_instructor
        FOREIGN KEY (instructor_id) REFERENCES users(id)
);
