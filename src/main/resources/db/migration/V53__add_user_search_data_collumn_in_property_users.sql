ALTER TABLE property_users
ADD COLUMN user_search_data_id BIGINT,
ADD CONSTRAINT fk_user_search_data
    FOREIGN KEY (user_search_data_id)
    REFERENCES user_search_data(id);