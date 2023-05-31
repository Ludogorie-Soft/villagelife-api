package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChildrenUpTo14Years {

    Below10Years ("below 10"),
    From11To20Years ("11 - 20"),
    From21To50Years ("21 - 50"),
    Over50Years ("over 50");

    private final String name;

}
