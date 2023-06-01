CREATE TABLE IF NOT EXISTS village_population_assertion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    populated_assertion_id BIGINT,
    answer VARCHAR(255) NOT NULL,
    FOREIGN KEY (village_id) REFERENCES villages(id),
    FOREIGN KEY (populated_assertion_id) REFERENCES populated_assertion(id)
);
