package com.example.ludogoriesoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddVillageFormResult {
    private VillageDTO villageDTO;
    private PopulationDTO populationDTO;
    private String groundCategoryName;
    private List<Long> ethnicityDTOIds;
    private List<String> questionResponses;
    private List<VillageLivingConditionDTO> villageLivingConditionDTOS;
    private List<ObjectVillageDTO> objectVillageDTOS;
    private List<VillagePopulationAssertionDTO> villagePopulationAssertionDTOS;
}
