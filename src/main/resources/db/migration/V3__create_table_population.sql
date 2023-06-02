CREATE TABLE IF NOT EXISTS populations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    number_of_population VARCHAR(255),
    residents VARCHAR(255),
    children VARCHAR(255),
    foreigners VARCHAR(255)
);