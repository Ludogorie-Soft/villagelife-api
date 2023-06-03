package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.enums.Consents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageLivingConditionDTO {

    private Long id;
    private Long villageId;
    private Long livingConditionId;
    private Consents consents;
}
