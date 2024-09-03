package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OwnershipType {
    INDIVIDUAL("Частно лице"),
    AGENCY("Агенция"),
    BUILDER("Строител"),
    INVESTOR("Инвеститор");
    private final String name;
}
