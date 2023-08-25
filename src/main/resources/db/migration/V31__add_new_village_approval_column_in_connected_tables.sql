ALTER TABLE village_ethnicities
ADD COLUMN village_status TINYINT(1) NOT NULL;
ALTER TABLE village_answer_question
ADD COLUMN village_status TINYINT(1) NOT NULL;
ALTER TABLE village_ground_categories
ADD COLUMN village_status TINYINT(1) NOT NULL;
ALTER TABLE village_images
ADD COLUMN village_status TINYINT(1) NOT NULL;
ALTER TABLE village_living_conditions
ADD COLUMN village_status TINYINT(1) NOT NULL;
ALTER TABLE village_objects
ADD COLUMN village_status TINYINT(1) NOT NULL;
ALTER TABLE village_population_assertion
ADD COLUMN village_status TINYINT(1) NOT NULL;
ALTER TABLE populations
ADD COLUMN village_status TINYINT(1) NOT NULL;