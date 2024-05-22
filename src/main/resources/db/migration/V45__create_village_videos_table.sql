CREATE TABLE IF NOT EXISTS village_videos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    url VARCHAR(255) NOT NULL,
    date_upload DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_deleted DATETIME DEFAULT NULL,
    village_status TINYINT(1) NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (village_id) REFERENCES villages(id)
);
