CREATE TABLE IF NOT EXISTS property_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_name VARCHAR(255) NOT NULL,
    property_id BIGINT,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (property_id) REFERENCES properties(id)
);