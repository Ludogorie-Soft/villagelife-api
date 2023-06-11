package com.example.ludogoriesoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NumberOfPopulation {

    UP_TO_10_PEOPLE ("до 10 човека", 1),
    FROM_11_TO_50_PEOPLE("11 - 50 човека", 2),
    FROM_51_TO_200_PEOPLE("51 - 200 човека", 3),
    FROM_201_TO_500_PEOPLE("201 - 500 човека", 4),
    FROM_501_TO_1000_PEOPLE("501 - 1000 човека", 5),
    FROM_1001_TO_2000_PEOPLE("1001 - 2000 човека", 6),
    FROM_2000_PEOPLE ("над 2000 човека", 7);

    private final String name;
    private final int valueAsNumber;

}
