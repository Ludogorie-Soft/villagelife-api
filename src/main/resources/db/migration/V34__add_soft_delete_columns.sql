ALTER TABLE village_ethnicities
ADD COLUMN date_deleted DATETIME DEFAULT NULL;
ALTER TABLE village_answer_question
ADD COLUMN date_deleted DATETIME DEFAULT NULL;
ALTER TABLE village_ground_categories
ADD COLUMN date_deleted DATETIME DEFAULT NULL;
ALTER TABLE village_images
ADD COLUMN date_deleted DATETIME DEFAULT NULL;
ALTER TABLE village_living_conditions
ADD COLUMN date_deleted DATETIME DEFAULT NULL;
ALTER TABLE village_objects
ADD COLUMN date_deleted DATETIME DEFAULT NULL;
ALTER TABLE village_population_assertion
ADD COLUMN date_deleted DATETIME DEFAULT NULL;