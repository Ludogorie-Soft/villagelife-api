package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyType {

    PLOT("парцел"),
    AGRICULTURAL_LAND("земеделска земя"),
    HOUSE("къща"),
    COTTAGE("вила"),
    FLOOR_OF_HOUSE("етаж от къща"),
    BUSINESS_PROPERTY("бизнес имот"),
    APARTMENT("апартамент");

    private final String name;
}
