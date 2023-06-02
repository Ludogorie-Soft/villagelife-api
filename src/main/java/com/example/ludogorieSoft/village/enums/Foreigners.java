package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Foreigners {

    Yes ("Да", 1),
    IDontKnow("Не знам", 2),
    No("Не", 3);

    private final String name;
    private final int valueAsNumber;
}
