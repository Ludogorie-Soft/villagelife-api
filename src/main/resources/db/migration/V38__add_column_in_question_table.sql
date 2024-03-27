ALTER TABLE questions
ADD COLUMN if not exists inform_message VARCHAR(255);
UPDATE questions
SET inform_message =
    CASE
        WHEN id = 1 THEN 'Култури и животни виреещи най-добре в селото:'
        WHEN id = 2 THEN 'Компании (възможности за работа) в радиус до 20 км около селото:'
        WHEN id = 3 THEN 'Природни забележителности в радиус до 15 км от селото:'
        WHEN id = 4 THEN 'Исторически забележителности в радиус до 15 км от селото:'
        WHEN id = 5 THEN 'Минимална сума на месец с която може да се живее в селото:'
        WHEN id = 6 THEN 'Кратка история на селото:'
        WHEN id = 7 THEN 'Актуални събития, провеждащи се през годината:'
        WHEN id = 8 THEN 'Жители желаещи да помогнат на нови хора да се заселят в селото:'
    END;

