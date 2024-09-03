CREATE TABLE IF NOT EXISTS agencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    job_title VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(25),
    address VARCHAR(255),
    website_name VARCHAR(100),
    website_link VARCHAR(255),
    number_of_agents INT NOT NULL CHECK (number_of_agents >= 0),
    deleted_at TIMESTAMP NULL
);
