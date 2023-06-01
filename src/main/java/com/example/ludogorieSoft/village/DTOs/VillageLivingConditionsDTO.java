package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Enums.Consents;
import com.example.ludogorieSoft.village.Model.LivingConditions;
import com.example.ludogorieSoft.village.Model.Village;
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
