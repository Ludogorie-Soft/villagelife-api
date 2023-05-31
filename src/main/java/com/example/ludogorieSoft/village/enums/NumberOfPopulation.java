package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NumberOfPopulation {

    UpTo10People ("до 10 човека"),
    From11To50People("11 - 50 човека"),
    From51To200People("51 - 200 човека"),
    From201To500People("201 - 500 човека"),
    From501To1000People("501 - 1000 човека"),
    From1001To2000People("1001 - 2000 човека"),
    Over2000People ("над 2000 човека");

    private final String name;

}
