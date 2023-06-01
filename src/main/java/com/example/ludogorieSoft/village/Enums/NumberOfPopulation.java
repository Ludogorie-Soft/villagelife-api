package com.example.ludogorieSoft.village.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NumberOfPopulation {

    UpTo10People ("до 10 човека", 1),
    From11To50People("11 - 50 човека", 2),
    From51To200People("51 - 200 човека", 3),
    From201To500People("201 - 500 човека", 4),
    From501To1000People("501 - 1000 човека", 5),
    From1001To2000People("1001 - 2000 човека", 6),
    Over2000People ("над 2000 човека", 7);

    private final String name;
    private final int valueAsNumber;

}
