package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyType {
    PLOT("property.type.plot"),
    AGRICULTURAL_LAND("property.type.agricultural.land"),
    HOUSE("property.type.house"),
    VILLA("property.type.villa"),
    FLOOR_OF_A_HOUSE("property.type.floor.of.a.house"),
    BUSINESS_PROPERTY("property.type.business.property"),
    APARTMENT("property.type.apartment");
    private final String key;
}
