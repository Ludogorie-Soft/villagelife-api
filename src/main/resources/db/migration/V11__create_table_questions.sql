CREATE TABLE IF NOT EXISTS questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_name VARCHAR(255) NOT NULL,
    UNIQUE (question_name)
);