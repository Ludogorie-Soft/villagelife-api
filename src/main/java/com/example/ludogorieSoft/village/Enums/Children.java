package com.example.ludogorieSoft.village.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Children {

    Below10Years  ("под 10", 1),
    From11To20Years ("11 - 20", 2),
    From21To50Years ("21 - 50", 3),
    Over50Years ("над 50", 4);

    private final String name;
    private final int valueAsNumber;

}
