CREATE TABLE quiz_questions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    text TEXT NOT NULL,
    correct_option_index INTEGER NOT NULL,

    CONSTRAINT fk_question_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
        ON DELETE CASCADE
);
