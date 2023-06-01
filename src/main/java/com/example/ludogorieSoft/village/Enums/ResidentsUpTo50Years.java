package com.example.ludogorieSoft.village.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResidentsUpTo50Years {

    UpTo2Percent ("до 2%", 1),
    From2To5Percent ("2% - 5%", 2),
    From6To10Percent ("6% - 10%", 3),
    From11To20Percent ("11% - 20%", 4),
    From21To30Percent ("21% - 30%", 5),
    Over30Percent ("над 30%", 6);

    private final String name;
    private final int valueAsNumber;


}
