CREATE TABLE quizzes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,

    CONSTRAINT fk_quiz_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE CASCADE
);
