package com.example.ludogorieSoft.village.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Foreigners {

    Yes ("Да"),
    IDontKnow("Не знам"),
    No("Не");

    private final String name;
}
