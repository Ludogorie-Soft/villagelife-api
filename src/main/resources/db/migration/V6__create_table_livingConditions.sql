CREATE TABLE IF NOT EXISTS living_conditions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    living_condition_name VARCHAR(255),
    UNIQUE (`living_condition_name`)
);