CREATE TABLE IF NOT EXISTS living_conditions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conditions VARCHAR(255),
    UNIQUE (conditions)
);