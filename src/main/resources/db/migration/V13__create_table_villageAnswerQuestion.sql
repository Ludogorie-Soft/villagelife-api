CREATE TABLE IF NOT EXISTS village_answer_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    questions_id BIGINT,
    answer VARCHAR(255),
    FOREIGN KEY (village_id) REFERENCES villages(id),
    FOREIGN KEY (questions_id) REFERENCES questions(id)
);
