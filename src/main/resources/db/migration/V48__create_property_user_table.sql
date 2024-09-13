CREATE TABLE IF NOT EXISTS property_users  (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ownership_type ENUM('INDIVIDUAL', 'AGENCY', 'BUILDER', 'INVESTOR') NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(25) NOT NULL,
    user_search_data_id BIGINT,
    password VARCHAR(255) NOT NULL,
    job_title VARCHAR(255) NOT NULL,
    business_card_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_search_data_id) REFERENCES user_search_data(id),
    FOREIGN KEY (business_card_id) REFERENCES business_cards(id)
);
