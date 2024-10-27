package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstructionType {
    BRICKS("construction.type.bricks"),
    PANEL("construction.type.panel"),
    WOOD("construction.type.wood");
    private final String key;
}
