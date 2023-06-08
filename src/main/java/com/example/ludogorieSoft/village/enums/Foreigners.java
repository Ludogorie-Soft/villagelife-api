package com.example.ludogoriesoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Foreigners {

    YES ("Да", 1),
    I_DONT_KNOW("Не знам", 2),
    NO("Не", 3);

    private final String name;
    private final int valueAsNumber;
}
