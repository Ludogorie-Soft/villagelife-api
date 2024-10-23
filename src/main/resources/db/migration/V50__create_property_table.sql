CREATE TABLE IF NOT EXISTS properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    village_id BIGINT,
    user_id BIGINT,
    property_type ENUM('PLOT', 'AGRICULTURAL_LAND', 'HOUSE', 'VILLA', 'FLOOR_OF_A_HOUSE', 'BUSINESS_PROPERTY', 'APARTMENT') NOT NULL,
    property_transfer_type ENUM('SALE', 'RENT') NOT NULL,
    price DECIMAL(19, 2),
    phone_number VARCHAR(255) NOT NULL,
    build_up_area DOUBLE NOT NULL,
    yard_area DOUBLE NOT NULL,
    rooms_count INT NOT NULL,
    bathrooms_count INT NOT NULL,
    image_url VARCHAR(255),
    construction_type ENUM('BRICKS', 'PANEL', 'WOOD') NOT NULL,
    construction_year VARCHAR(4),
    extras TEXT,
    stats_id BIGINT,
    ownership_type ENUM('INDIVIDUAL', 'AGENCY', 'BUILDER', 'INVESTOR') NOT NULL,
    property_condition ENUM('NEW', 'EXCELLENT', 'GOOD', 'FAIR', 'POOR', 'UNDER_CONSTRUCTION', 'DAMAGED', 'RENOVATION_REQUIRED') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deactivated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (village_id) REFERENCES villages(id),
    FOREIGN KEY (user_id) REFERENCES property_users(id),
    FOREIGN KEY (stats_id) REFERENCES property_stats(id)
);

CREATE TABLE IF NOT EXISTS property_heating(
    property_id BIGINT,
    heating VARCHAR(255),
    PRIMARY KEY (property_id, heating),
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE
);