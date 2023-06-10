package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddVillageFormResult {
    VillageDTO villageDTO;
    PopulationDTO populationDTO;
    String groundCategoryName;
    List<ObjectAroundVillageDTO> objectAroundVillageDTOS;
}
