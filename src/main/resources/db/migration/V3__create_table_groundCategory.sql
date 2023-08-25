CREATE TABLE IF NOT EXISTS ground_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ground_category_name VARCHAR(255) NOT NULL,
    UNIQUE (ground_category_name)
);