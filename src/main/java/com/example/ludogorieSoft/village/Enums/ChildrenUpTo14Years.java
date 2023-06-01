package com.example.ludogorieSoft.village.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChildrenUpTo14Years {

    Below10Years ("под 10"),
    From11To20Years ("11 - 20"),
    From21To50Years ("21 - 50"),
    Over50Years ("над 50");

    private final String name;

}
