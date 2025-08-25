CREATE TABLE lessons(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content_url VARCHAR(200) NOT NULL,
    content_type VARCHAR(25) NOT NULL,
    order_index INTEGER NOT NULL,
    duration INTERVAL NOT NULL,

    CONSTRAINT fk_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE CASCADE
);