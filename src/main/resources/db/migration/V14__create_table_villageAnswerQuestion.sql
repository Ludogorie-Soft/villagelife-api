CREATE TABLE IF NOT EXISTS village_answer_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    question_id BIGINT,
    answer VARCHAR(255),
    FOREIGN KEY (village_id) REFERENCES villages(id),
    FOREIGN KEY (question_id) REFERENCES questions(id)
);
