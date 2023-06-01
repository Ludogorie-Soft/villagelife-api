CREATE TABLE IF NOT EXISTS village_ground_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    ground_category_id BIGINT,
    FOREIGN KEY (village_id) REFERENCES villages(id),
    FOREIGN KEY (ground_category_id) REFERENCES ground_category(id)
);
