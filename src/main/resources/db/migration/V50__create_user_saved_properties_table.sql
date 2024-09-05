CREATE TABLE IF NOT EXISTS user_saved_properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    property_id BIGINT,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES property_users(id),
    FOREIGN KEY (property_id) REFERENCES properties(id)
);