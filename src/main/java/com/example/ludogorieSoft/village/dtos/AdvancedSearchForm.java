package com.example.ludogoriesoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdvancedSearchForm {
    private List<LivingConditionDTO> livingConditionDTOS;
    private List<ObjectAroundVillageDTO> objectAroundVillageDTOS;

//    private List<PopulatedAssertionDTO> populatedAssertionDTOS;

    private List<PopulationDTO> populationDTOS;
}
