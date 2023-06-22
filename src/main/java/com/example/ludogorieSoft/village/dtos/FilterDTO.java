package com.example.ludogoriesoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDTO {

    private List<LivingConditionDTO> livingConditionDTOS;
    private List<ObjectAroundVillageDTO> objectAroundVillageDTOS;
    private List<PopulationDTO> populationDTOS;
}