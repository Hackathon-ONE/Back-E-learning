CREATE TABLE quiz_options (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    question_id BIGINT NOT NULL,
    text TEXT NOT NULL,
    option_index INTEGER NOT NULL,

    CONSTRAINT fk_option_question
        FOREIGN KEY (question_id) REFERENCES quiz_questions(id)
        ON DELETE CASCADE
);