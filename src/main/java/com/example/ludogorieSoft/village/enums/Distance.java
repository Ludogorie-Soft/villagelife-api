package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Distance {
    IN_THE_VILLAGE("в селото", 100),
    ON_10_KM("на 10 км", 80),
    ON_11_TO_30KM("на 11-30 км", 60),
    ON_31_TO_50_KM("на 31-50 км", 40),
    OVER_50_KM("над 50 км", 20);

    private final String name;
    private final int value;
}
