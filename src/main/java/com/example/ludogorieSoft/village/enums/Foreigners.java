package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Foreigners {

    Yes ("Да"),
    IDontKnow("Не знам"),
    No("не");

    private final String name;
}
