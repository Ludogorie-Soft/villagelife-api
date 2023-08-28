package com.example.ludogorieSoft.village.enums;

import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Residents {

    UP_TO_2_PERCENT ("до 2%", 1),
    FROM_2_TO_5_PERCENT ("2% - 5%", 2),
    FROM_6_TO_10_PERCENT ("6% - 10%", 3),
    FROM_11_TO_20_PERCENT ("11% - 20%", 4),
    FROM_21_TO_30_PERCENT ("21% - 30%", 5),
    OVER_30_PERCENT ("над 30%", 6);

    private final String name;
    private final int valueAsNumber;

    public static Residents getByValueAsNumber(int valueAsNumber){
        List<Residents> residentsList = List.of(values());
        Residents residents = null;
        for(Residents residentsResult : residentsList) {
            if (residentsResult.getValueAsNumber() == valueAsNumber) {
                residents = residentsResult;
            }
        }
        if (residents != null){
            return residents;
        }
        throw new ApiRequestException("Residents not found");
    }
}
