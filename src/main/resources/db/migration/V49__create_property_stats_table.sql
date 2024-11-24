CREATE TABLE IF NOT EXISTS property_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seen_in_results BIGINT NOT NULL,
    views BIGINT NOT NULL,
    shares BIGINT NOT NULL,
    saves BIGINT NOT NULL,
    deleted_at TIMESTAMP NULL
);
