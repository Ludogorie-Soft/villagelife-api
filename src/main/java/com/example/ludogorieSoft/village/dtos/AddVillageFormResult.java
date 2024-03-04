package com.example.ludogorieSoft.village.dtos;

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
    private List<Long> groundCategoryIds;
    private List<Long> ethnicityDTOIds;
    private List<String> questionResponses;
    private List<VillageLivingConditionDTO> villageLivingConditionDTOS;
    private List<ObjectVillageDTO> objectVillageDTOS;
    private List<VillagePopulationAssertionDTO> villagePopulationAssertionDTOS;
    private List<byte[]> imageBytes;
    private UserDTO userDTO;
}
