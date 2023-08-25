CREATE TABLE IF NOT EXISTS populations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    population_count INT NOT NULL,
    number_of_population VARCHAR(255),
    residents VARCHAR(255),
    children VARCHAR(255),
    foreigners VARCHAR(255),
    FOREIGN KEY (village_id) REFERENCES villages(id)
);