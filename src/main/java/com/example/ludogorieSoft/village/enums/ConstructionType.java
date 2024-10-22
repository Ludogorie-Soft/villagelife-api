package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstructionType {
/*
            тухла
    English: brick
    German: Ziegel

            градоред
    English: timber-framed
    German: Fachwerk

            керпич
    English: adobe (or sun-dried brick)
    German: Lehmziegel

            стоманобетон
    English: reinforced concrete
    German: Stahlbeton

            камък
    English: stone
    German: Stein

            глина
    English: clay
    German: Ton
    */
    BRICKS("Тухли"),
    PANEL("Панел"),
    WOOD("Дърво"),
    TIMBER_FRAMED("Градоред"),
    ADOBE("Керпич"),
    REINFORCED_CONCRETE("Стоманобетон"),
    STONE("Камък"),
    CLAY("Глина");

    private final String name;
}
