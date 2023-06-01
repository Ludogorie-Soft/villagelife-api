CREATE TABLE IF NOT EXISTS village_living_conditions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    living_conditions_id BIGINT,
    consents VARCHAR(255),
    FOREIGN KEY (village_id) REFERENCES villages(id),
    FOREIGN KEY (living_conditions_id) REFERENCES living_conditions(id)
);
