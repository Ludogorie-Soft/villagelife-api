package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResidentsUpTo50Years {

    UpTo2Percent ("Up to 2%"),
    From2To5Percent ("2% - 5%"),
    From6To10Percent ("6% - 10%"),
    From11To20Percent ("11% - 20%"),
    From21To30Percent ("21% - 30%"),
    Over30Percent ("over 30%");

    private final String name;


}
