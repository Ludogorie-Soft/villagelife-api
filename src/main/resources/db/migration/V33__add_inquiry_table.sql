CREATE TABLE IF NOT EXISTS inquiries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    user_message VARCHAR(700) NOT NULL,
    mobile VARCHAR(255),
    village_id BIGINT,
    inquiry_type VARCHAR(255),
    FOREIGN KEY (village_id) REFERENCES villages (id)
);