CREATE TABLE IF NOT EXISTS business_card_logos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_name VARCHAR(255) NOT NULL,
    deleted_at TIMESTAMP NULL
);

ALTER TABLE business_cards
    ADD COLUMN business_card_logo_id BIGINT;

ALTER TABLE business_cards
    ADD CONSTRAINT fk_business_cards_business_card_logo_id FOREIGN KEY (business_card_logo_id) REFERENCES business_card_logos(id) ON DELETE CASCADE;