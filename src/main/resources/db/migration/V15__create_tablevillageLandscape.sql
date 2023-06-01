CREATE TABLE IF NOT EXISTS village_landscape (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    landscape_id BIGINT,
    FOREIGN KEY (village_id) REFERENCES villages(id),
    FOREIGN KEY (landscape_id) REFERENCES landscapes(id)
);
