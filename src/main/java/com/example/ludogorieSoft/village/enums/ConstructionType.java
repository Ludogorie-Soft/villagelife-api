package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstructionType {
    BRICKS("construction.type.bricks"),
    PANEL("construction.type.panel"),
    WOOD("construction.type.wood"),
    TIMBER_FRAMED("construction.type.timber.framed"),
    ADOBE("construction.type.adobe"),
    REINFORCED_CONCRETE("construction.type.reinforced.concrete"),
    STONE("construction.type.stone"),
    CLAY("construction.type.clay");

    private final String name;
}
