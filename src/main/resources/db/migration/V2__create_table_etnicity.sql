CREATE TABLE IF NOT EXISTS ethnicities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ethnicity_name VARCHAR(255) NOT NULL,
    UNIQUE (ethnicity_name)
);