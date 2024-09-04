package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyType {
    PLOT("Парцел"),
    AGRICULTURAL_LAND("Земеделска земя"),
    HOUSE("Къща"),
    VILLA("Вила"),
    FLOOR_OF_A_HOUSE("Етаж от къща"),
    BUSINESS_PROPERTY("Бизнес имот"),
    APARTMENT("Апартамент");
    private final String name;


}
