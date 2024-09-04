package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstructionType {

    BRICK("тухла"),
    PANEL("панрел"),
    LARGE_AREA_FORMWORK("ЕПК");

    private final String name;
}
