package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NumberOfPopulation {

    UpTo10People ("Up to 10 people"),
    From11To50People("11 - 50 people"),
    From51To200People("51 - 200 people"),
    From201To500People("201 - 500 people"),
    From501To1000People("501 - 1000 people"),
    From1001To2000People("1001 - 2000 people"),
    Over2000People ("over 2000 people");

    private final String name;

}
