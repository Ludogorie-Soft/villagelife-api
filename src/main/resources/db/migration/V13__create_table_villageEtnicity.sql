CREATE TABLE IF NOT EXISTS village_ethnicities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    ethnicity_id BIGINT,
    FOREIGN KEY (village_id) REFERENCES villages (id),
    FOREIGN KEY (ethnicity_id) REFERENCES ethnicities (id)
);