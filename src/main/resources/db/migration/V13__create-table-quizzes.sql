CREATE TABLE quizzes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    course_id BIGINT,
    lesson_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT fk_quiz_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_quiz_lesson
        FOREIGN KEY (lesson_id) REFERENCES lessons(id)
        ON DELETE CASCADE
);