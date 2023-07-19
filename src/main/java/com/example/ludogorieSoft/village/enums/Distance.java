package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Distance {
    IN_THE_VILLAGE("в селото"),
    ON_10_KM("на 10 км"),
    ON_11_TO_30KM("на 11-30 км"),
    ON_31_TO_50_KM("на 31-50 км"),
    OVER_50_KM("над 50 км");

    private final String name;
}
