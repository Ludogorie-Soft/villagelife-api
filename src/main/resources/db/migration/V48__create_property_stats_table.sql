CREATE TABLE IF NOT EXISTS property_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seen_in_results BIGINT,
    views INT NOT NULL,
    shares INT NOT NULL,
    saves INT NOT NULL,
    deleted_at TIMESTAMP NULL
);
