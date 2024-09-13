CREATE TABLE IF NOT EXISTS user_search_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    village_id BIGINT,
    property_type ENUM('PLOT', 'AGRICULTURAL_LAND', 'HOUSE', 'VILLA', 'FLOOR_OF_A_HOUSE', 'BUSINESS_PROPERTY', 'APARTMENT') NOT NULL,
    property_transfer_type ENUM('SALE', 'RENT') NOT NULL,
    min_built_up_area DOUBLE CHECK (min_built_up_area >= 0),
    max_built_up_area DOUBLE CHECK (max_built_up_area >= 0),
    min_yard_area DOUBLE CHECK (min_yard_area >= 0),
    max_yard_area DOUBLE CHECK (max_yard_area >= 0),
    min_rooms_count SMALLINT CHECK (min_rooms_count >= 0),
    max_rooms_count SMALLINT CHECK (max_rooms_count >= 0),
    min_bathrooms_count SMALLINT CHECK (min_bathrooms_count >= 0),
    max_bathrooms_count SMALLINT CHECK (max_bathrooms_count >= 0),
    heating VARCHAR(255),
    construction_type ENUM('BRICKS', 'PANEL', 'WOOD') NOT NULL,
    min_construction_year SMALLINT CHECK (min_construction_year >= 0),
    max_construction_year SMALLINT CHECK (max_construction_year >= 0),
    extras TEXT,
    min_price DECIMAL(19, 2) CHECK (min_price >= 0),
    max_price DECIMAL(19, 2) CHECK (max_price >= 0),
    ownership_type ENUM('INDIVIDUAL', 'AGENCY', 'BUILDER', 'INVESTOR') NOT NULL,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (village_id) REFERENCES villages(id)
);