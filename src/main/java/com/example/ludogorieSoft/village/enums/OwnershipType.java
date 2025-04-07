package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OwnershipType {
    INDIVIDUAL("ownership.type.individual"),
    AGENCY("ownership.type.agency"),
    BUILDER("ownership.type.builder"),
    INVESTOR("ownership.type.investor");
    private final String key;
}
