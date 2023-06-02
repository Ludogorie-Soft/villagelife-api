package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Distance {
    InTheVillage("в селото"),
    On10km("на 10 км"),
    On11to30km("на 11-30 км"),
    On31to50km("на 31-50 км"),
    Over50km("над 50 км.");

    private final String name;
}
