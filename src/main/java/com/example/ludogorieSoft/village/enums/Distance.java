package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Distance {
    IN_THE_VILLAGE("distance.in_the_village", 100),
    ON_10_KM("distance.on_10_km", 80),
    ON_11_TO_30KM("distance.on_11_to_30km", 60),
    ON_31_TO_50_KM("distance.on_31_to_50_km", 40),
    OVER_50_KM("distance.over_50_km", 20);

    private final String name;
    private final int value;
}
