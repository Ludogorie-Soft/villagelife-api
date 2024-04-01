UPDATE `ethnicities`
SET `ethnicity_name` =
    CASE
        WHEN `id` = 1 THEN 'ethnicities.roma'
        WHEN `id` = 2 THEN 'ethnicities.turks'
        WHEN `id` = 3 THEN 'ethnicities.vlasi'
        WHEN `id` = 4 THEN 'ethnicities.armenian'
        WHEN `id` = 5 THEN 'ethnicities.karakachani'
        WHEN `id` = 6 THEN 'ethnicities.greeks'
        WHEN `id` = 7 THEN 'ethnicities.tatars'
        WHEN `id` = 8 THEN 'ethnicities.jews'
        WHEN `id` = 9 THEN 'ethnicities.noEthnicities'
    END;
