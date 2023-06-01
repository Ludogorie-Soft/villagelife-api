CREATE TABLE IF NOT EXISTS populations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    number_of_population bigint,
    residents_up_to_50_years bigint,
    children_up_to_14_years bigint,
    foreigners bigint
);