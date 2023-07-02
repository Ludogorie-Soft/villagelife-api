CREATE TABLE IF NOT EXISTS village_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    image_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (village_id) REFERENCES villages(id)
);
