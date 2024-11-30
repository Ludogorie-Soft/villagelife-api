CREATE TABLE IF NOT EXISTS verification_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255),
    expiry_date TIMESTAMP,
    alternative_user_id BIGINT,
    FOREIGN KEY (alternative_user_id) REFERENCES alternative_users(id)
);