CREATE TABLE IF NOT EXISTS landscapes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    landscape_name VARCHAR(255) NOT NULL,
    UNIQUE (landscape_name)
);