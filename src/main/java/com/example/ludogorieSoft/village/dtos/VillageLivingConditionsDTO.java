package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.model.LivingConditions;
import com.example.ludogorieSoft.village.model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageLivingConditionsDTO {

    private Long id;
    private Village village;
    private LivingConditions livingConditions;
    private Consents consents;
}
