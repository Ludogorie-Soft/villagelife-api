RENAME TABLE admins TO alternative_users;

ALTER TABLE alternative_users
  ADD COLUMN deleted_at TIMESTAMP NULL,
  ADD COLUMN job_title VARCHAR(255) NULL,
  ADD COLUMN user_search_data_id BIGINT NULL,
  ADD COLUMN business_card_id BIGINT NULL;

ALTER TABLE alternative_users
  ADD CONSTRAINT fk_user_search_data_id FOREIGN KEY (user_search_data_id) REFERENCES user_search_data(id),
  ADD CONSTRAINT fk_business_card_id FOREIGN KEY (business_card_id) REFERENCES business_cards(id);

ALTER TABLE villages
  DROP FOREIGN KEY fk_villages_admin;
ALTER TABLE villages
  ADD CONSTRAINT fk_villages_alternative_user_id FOREIGN KEY (admin_id) REFERENCES alternative_users(id);
