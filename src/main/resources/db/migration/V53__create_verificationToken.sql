CREATE TABLE IF NOT EXISTS verification_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255),
    expiry_date TIMESTAMP,
    property_user_id BIGINT,
    FOREIGN KEY (property_user_id) REFERENCES property_users(id)
);

ALTER TABLE property_users
ADD COLUMN enabled TINYINT(1) DEFAULT 0;