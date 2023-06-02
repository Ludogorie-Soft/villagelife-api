CREATE TABLE IF NOT EXISTS villages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    population_id BIGINT,
    date_upload DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TINYINT(1) NOT NULL,
    admin_id BIGINT,
    date_approved DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_villages_population FOREIGN KEY (population_id) REFERENCES populations(id),
    CONSTRAINT fk_villages_admin FOREIGN KEY (admin_id) REFERENCES admins(id)
);
