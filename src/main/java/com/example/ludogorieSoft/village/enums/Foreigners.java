package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Foreigners {

    YES ("foreigners.yes", 1),
    I_DONT_KNOW("foreigners.i_dont_know", 2),
    NO("foreigners.no", 3);

    private final String name;
    private final int valueAsNumber;
}
