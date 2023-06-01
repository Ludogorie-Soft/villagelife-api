CREATE TABLE IF NOT EXISTS village_objects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    object_id BIGINT,
    FOREIGN KEY (village_id) REFERENCES villages (id),
    FOREIGN KEY (object_id) REFERENCES objects (id)
);