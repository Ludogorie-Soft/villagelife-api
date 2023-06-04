CREATE TABLE IF NOT EXISTS populated_assertion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    populated_assertion_name VARCHAR(255),
    UNIQUE (populated_assertion_name)
);
