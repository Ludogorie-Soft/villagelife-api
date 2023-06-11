package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddVillageFormResultService {
    private final PopulationService populationService;
    private final VillageService villageService;
    private final VillageGroundCategoryService villageGroundCategoryService;
    private final EthnicityVillageService ethnicityVillageService;
    private final EthnicityService ethnicityService;
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

        List<Long> ethnicityDTOIds = addVillageFormResult.getEthnicityDTOIds();
        for (Long id : ethnicityDTOIds) {
            EthnicityVillageDTO ethnicityVillageDTO = new EthnicityVillageDTO(null, villageDTO.getId(), id);
            ethnicityVillageService.createEthnicityVillage(ethnicityVillageDTO);
        }

        return addVillageFormResult;
    }
}
