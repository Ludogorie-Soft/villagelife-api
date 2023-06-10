package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.model.GroundCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddVillageFormResultService {
    private final PopulationService populationService;
    private final VillageService villageService;
    private final VillageGroundCategoryService villageGroundCategoryService;
    private final GroundCategoryService groundCategoryService;

    public AddVillageFormResult create(AddVillageFormResult addVillageFormResult){
        PopulationDTO populationDTO = addVillageFormResult.getPopulationDTO();
        PopulationDTO savedPopulation = populationService.createPopulation(populationDTO);

        VillageDTO villageDTO = addVillageFormResult.getVillageDTO();
        villageDTO.setPopulationDTO(savedPopulation);
        VillageDTO savedVillage = villageService.createVillage(villageDTO);

        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO.setVillageId(savedVillage.getId());
        GroundCategoryDTO groundCategoryDTO = groundCategoryService.getByGroundCategoryName(addVillageFormResult.getGroundCategoryName());
        villageGroundCategoryDTO.setGroundCategoryId(groundCategoryDTO.getId());
        villageGroundCategoryService.createVillageGroundCategoryDTO(villageGroundCategoryDTO);

        return addVillageFormResult;
    }
}
