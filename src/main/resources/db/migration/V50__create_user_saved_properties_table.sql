CREATE TABLE IF NOT EXISTS user_saved_properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    property_id BIGINT,
    deleted_at TIMESTAMP NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES property_user(id),
    CONSTRAINT fk_property
        FOREIGN KEY (property_id)
        REFERENCES property(id)
);