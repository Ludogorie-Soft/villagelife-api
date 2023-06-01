CREATE TABLE IF NOT EXISTS population (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    number_of_population VARCHAR(255),
    residents_up_to_50_years VARCHAR(255),
    children_up_to_14_years VARCHAR(255),
    foreigners VARCHAR(255)
);