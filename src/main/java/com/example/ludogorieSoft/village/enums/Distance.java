package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Distance {
    IN_THE_VILLAGE("distance.one", 100),
    ON_10_KM("distance.two", 80),
    ON_11_TO_30KM("distance.three", 60),
    ON_31_TO_50_KM("distance.four", 40),
    OVER_50_KM("distance.five", 20);

    private final String name;
    private final int value;
}
