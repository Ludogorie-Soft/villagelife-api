CREATE TABLE IF NOT EXISTS populated_assertion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    assertion VARCHAR(255),
    UNIQUE (assertion)
);
