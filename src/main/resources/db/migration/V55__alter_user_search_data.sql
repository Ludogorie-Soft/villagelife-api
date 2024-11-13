ALTER TABLE alternative_users
    DROP FOREIGN KEY fk_user_search_data_id;

ALTER TABLE alternative_users
    DROP COLUMN user_search_data_id;

ALTER TABLE user_search_data
    ADD COLUMN search_name VARCHAR(50) NOT NULL;

ALTER TABLE user_search_data
    ADD COLUMN alternative_user_id BIGINT,
    ADD CONSTRAINT fk_alternative_user_id FOREIGN KEY (alternative_user_id) REFERENCES alternative_users(id);

ALTER TABLE user_search_data
    DROP COLUMN property_type,
    DROP COLUMN construction_type,
    DROP COLUMN ownership_type,
    MODIFY COLUMN property_transfer_type ENUM('SALE', 'RENT') NULL;

ALTER TABLE user_search_data
    DROP COLUMN extras;

CREATE TABLE user_search_property_types (
    user_search_data_id BIGINT NOT NULL,
    property_type ENUM('PLOT','AGRICULTURAL_LAND','HOUSE','VILLA','FLOOR_OF_A_HOUSE','BUSINESS_PROPERTY','APARTMENT'),
    FOREIGN KEY (user_search_data_id) REFERENCES user_search_data(id)
);

CREATE TABLE user_search_construction_types (
    user_search_data_id BIGINT NOT NULL,
    construction_type ENUM('BRICKS','PANEL','WOOD'),
    FOREIGN KEY (user_search_data_id) REFERENCES user_search_data(id)
);

CREATE TABLE user_search_ownership_types (
    user_search_data_id BIGINT NOT NULL,
    ownership_type ENUM('INDIVIDUAL','AGENCY','BUILDER','INVESTOR'),
    FOREIGN KEY (user_search_data_id) REFERENCES user_search_data(id)
);

ALTER TABLE user_search_data
    DROP CONSTRAINT user_search_data_ibfk_1,
    DROP COLUMN village_id,
    ADD COLUMN village_name VARCHAR(255),
    ADD COLUMN region_name VARCHAR(255);
