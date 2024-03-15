UPDATE `regions` SET `region_name` =
    CASE
        WHEN `id` = 1 THEN 'regions.blagoevgrad'
        WHEN `id` = 2 THEN 'regions.burgas'
        WHEN `id` = 3 THEN 'regions.varna'
        WHEN `id` = 4 THEN 'regions.velikoTarnovo'
        WHEN `id` = 5 THEN 'regions.vidin'
        WHEN `id` = 6 THEN 'regions.vratsa'
        WHEN `id` = 7 THEN 'regions.gabrovo'
        WHEN `id` = 8 THEN 'regions.dobrich'
        WHEN `id` = 9 THEN 'regions.kardzhali'
        WHEN `id` = 10 THEN 'regions.kyustendil'
        WHEN `id` = 11 THEN 'regions.lovech'
        WHEN `id` = 12 THEN 'regions.montana'
        WHEN `id` = 13 THEN 'regions.pazardzhik'
        WHEN `id` = 14 THEN 'regions.pernik'
        WHEN `id` = 15 THEN 'regions.pleven'
        WHEN `id` = 16 THEN 'regions.plovdiv'
        WHEN `id` = 17 THEN 'regions.razgrad'
        WHEN `id` = 18 THEN 'regions.ruse'
        WHEN `id` = 19 THEN 'regions.silistra'
        WHEN `id` = 20 THEN 'regions.sliven'
        WHEN `id` = 21 THEN 'regions.smolyan'
        WHEN `id` = 22 THEN 'regions.sofiaRegion'
        WHEN `id` = 23 THEN 'regions.sofia'
        WHEN `id` = 24 THEN 'regions.staraZagora'
        WHEN `id` = 25 THEN 'regions.targovishte'
        WHEN `id` = 26 THEN 'regions.haskovo'
        WHEN `id` = 27 THEN 'regions.shumen'
        WHEN `id` = 28 THEN 'regions.yambol'
    END;


UPDATE populated_assertion
SET populated_assertion_name = CASE
    WHEN id = 1 THEN 'populated_assertion.kind.hearted'
    WHEN id = 2 THEN 'populated_assertion.cautious'
    WHEN id = 3 THEN 'populated_assertion.attentive'
    WHEN id = 4 THEN 'populated_assertion.communicative'
END
WHERE id IN (1, 2, 3, 4);
