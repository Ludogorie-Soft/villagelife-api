ALTER TABLE questions
ADD COLUMN if not exists inform_message VARCHAR(255);

UPDATE questions
SET question_name =
    CASE
        WHEN id = 1 THEN 'question_name.first'
        WHEN id = 2 THEN 'question_name.second'
        WHEN id = 3 THEN 'question_name.third'
        WHEN id = 4 THEN 'question_name.forth'
        WHEN id = 5 THEN 'question_name.fifth'
        WHEN id = 6 THEN 'question_name.sixth'
        WHEN id = 7 THEN 'question_name.seventh'
        WHEN id = 8 THEN 'question_name.eighth'
    END;

UPDATE questions
SET inform_message =
    CASE
        WHEN id = 1 THEN 'inform_message.first'
        WHEN id = 2 THEN 'inform_message.second'
        WHEN id = 3 THEN 'inform_message.third'
        WHEN id = 4 THEN 'inform_message.forth'
        WHEN id = 5 THEN 'inform_message.fifth'
        WHEN id = 6 THEN 'inform_message.sixth'
        WHEN id = 7 THEN 'inform_message.seventh'
        WHEN id = 8 THEN 'inform_message.eighth'
    END;
