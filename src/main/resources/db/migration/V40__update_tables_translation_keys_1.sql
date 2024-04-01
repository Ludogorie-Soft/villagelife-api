UPDATE ground_categories
SET ground_category_name =
    CASE
        WHEN id = 1 THEN 'ground_categories.first'
        WHEN id = 2 THEN 'ground_categories.second'
        WHEN id = 3 THEN 'ground_categories.third'
        WHEN id = 4 THEN 'ground_categories.fourth'
        WHEN id = 5 THEN 'ground_categories.fifth'
        WHEN id = 6 THEN 'ground_categories.sixth'
        WHEN id = 7 THEN 'ground_categories.seventh'
        WHEN id = 8 THEN 'ground_categories.eight'
        WHEN id = 9 THEN 'ground_categories.ninth'
        WHEN id = 10 THEN 'ground_categories.tenth'
        WHEN id = 11 THEN 'ground_categories.doNotKnow'
    END;

UPDATE living_conditions
SET living_condition_name =
    CASE
        WHEN id = 1 THEN 'living_condition.first'
        WHEN id = 2 THEN 'living_condition.second'
        WHEN id = 3 THEN 'living_condition.third'
        WHEN id = 4 THEN 'living_condition.fourth'
        WHEN id = 5 THEN 'living_condition.fifth'
        WHEN id = 6 THEN 'living_condition.sixth'
        WHEN id = 7 THEN 'living_condition.seventh'
        WHEN id = 8 THEN 'living_condition.eight'
        WHEN id = 9 THEN 'living_condition.ninth'
        WHEN id = 10 THEN 'living_condition.tenth'
        WHEN id = 11 THEN 'living_condition.eleventh'
        WHEN id = 12 THEN 'living_condition.twelfth'
        WHEN id = 13 THEN 'living_condition.thirteenth'
    END;

UPDATE objects
SET type =
    CASE
        WHEN id = 1 THEN 'objects.first'
        WHEN id = 2 THEN 'objects.second'
        WHEN id = 3 THEN 'objects.third'
        WHEN id = 4 THEN 'objects.fourth'
        WHEN id = 5 THEN 'objects.fifth'
        WHEN id = 6 THEN 'objects.sixth'
        WHEN id = 7 THEN 'objects.seventh'
        WHEN id = 8 THEN 'objects.eight'
        WHEN id = 9 THEN 'objects.ninth'
        WHEN id = 10 THEN 'objects.tenth'
        WHEN id = 11 THEN 'objects.eleventh'
        WHEN id = 12 THEN 'objects.twelfth'
        WHEN id = 13 THEN 'objects.thirteenth'
        WHEN id = 14 THEN 'objects.fourteenth'
    END;


