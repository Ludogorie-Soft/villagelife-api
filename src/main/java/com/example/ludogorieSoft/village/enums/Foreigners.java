package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Foreigners {

    Yes ("Yes"),
    IDontKnow("I don't know"),
    No("No");

    private final String name;
}
