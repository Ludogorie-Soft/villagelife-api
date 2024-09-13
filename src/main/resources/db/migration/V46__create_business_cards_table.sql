CREATE TABLE IF NOT EXISTS business_cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(25),
    address VARCHAR(255),
    website_link VARCHAR(255),
    number_of_employees INT CHECK (number_of_employees >= 0),
    deleted_at TIMESTAMP NULL
);