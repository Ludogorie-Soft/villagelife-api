package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdvancedSearchFormService {

    private LivingConditionService livingConditionService;

    private final ObjectAroundVillageService objectAroundVillageService;

//    private final PopulatedAssertionService populatedAssertionService;

    private final PopulationService populationService;


    public AdvancedSearchForm create(AdvancedSearchForm advancedSearchForm) {

        List<LivingConditionDTO> livingConditionDTOS = livingConditionService.getAllLivingConditions();
        advancedSearchForm.setLivingConditionDTOS(livingConditionDTOS);

        List<ObjectAroundVillageDTO> objectVillageDTOS = objectAroundVillageService.getAllObjectsAroundVillage();
        advancedSearchForm.setObjectAroundVillageDTOS(objectVillageDTOS);

//        List<PopulatedAssertionDTO> populatedAssertionDTOS = populatedAssertionService.getAllPopulatedAssertion();
//        advancedSearchFormResult.setPopulatedAssertionDTOS(populatedAssertionDTOS);

        List<PopulationDTO> populationDTOS = populationService.getAllPopulation();
        advancedSearchForm.setPopulationDTOS(populationDTOS);

        return advancedSearchForm;
    }





}
