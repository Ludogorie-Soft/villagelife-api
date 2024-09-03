package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@AllArgsConstructor
public enum OwnershipType {
    INDIVIDUAL("Частно лице"),
    AGENCY("Агенция"),
    BUILDER("Строител"),
    INVESTOR("Инвеститор");
    private final String name;
}
