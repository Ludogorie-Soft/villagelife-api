CREATE TABLE IF NOT EXISTS messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    user_message VARCHAR(700) NOT NULL
);