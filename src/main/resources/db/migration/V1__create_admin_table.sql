CREATE TABLE IF NOT EXISTS admins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    UNIQUE (full_name),
    UNIQUE (email),
    UNIQUE (username),
    UNIQUE (password),
    UNIQUE (mobile)
);