
CREATE TABLE IF NOT EXISTS populated_assertion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    populated_assertion_name VARCHAR(255) NOT NULL,
    UNIQUE (populated_assertion_name)

);
