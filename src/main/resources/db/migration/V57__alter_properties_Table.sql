ALTER TABLE properties DROP INDEX phone_number;
ALTER TABLE properties MODIFY phone_number VARCHAR(255) NOT NULL;