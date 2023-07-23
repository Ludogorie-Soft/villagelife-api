package com.example.ludogorieSoft.village.dtos.response;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.model.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VillageInfo {
    private VillageDTO villageDTO;
    private List<Ethnicity> ethnicities;
    private List<PopulationAssertionResponse> populationAssertionResponses;
    private List<LivingConditionResponse> livingConditionResponses;
    private List<ObjectVillage> objectVillages;
}
