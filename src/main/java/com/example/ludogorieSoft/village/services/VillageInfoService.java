package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.model.Village;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VillageInfoService {
    private final VillageService villageService;
    private final PopulatedAssertionService populatedAssertionService;
    private final LivingConditionService livingConditionService;
    private final ObjectVillageService objectVillageService;
    private final EthnicityVillageService ethnicityVillageService;
    private final VillageAnswerQuestionService villageAnswerQuestionService;
    private final PopulationService populationService;
    private final VillageGroundCategoryService villageGroundCategoryService;
    public VillageInfo getVillageInfoByVillageId(Long villageId, boolean status, String date){
        Village village = villageService.checkVillage(villageId);
        VillageInfo villageInfo = new VillageInfo();
        villageInfo.setVillageDTO(villageService.getVillageById(village.getId()));

        villageInfo.setPopulationDTO(populationService.getPopulationByVillageId(villageId, status, date));
        villageInfo.setPopulationAssertionResponses(populatedAssertionService.getPopulationAssertionResponse(village.getId(),status,date));
        villageInfo.setLivingConditionResponses(livingConditionService.getLivingConditionResponses(village.getId(),status,date));
        villageInfo.getLivingConditionResponses().add(livingConditionService.getAccessibilityByVillageId(village.getId(),status,date));
        villageInfo.getLivingConditionResponses().add(livingConditionService.getCrimeByVillageId(village.getId(),status,date));
        villageInfo.getLivingConditionResponses().add(livingConditionService.getTotalLivingConditionsByVillageId(village.getId(),status,date));
        villageInfo.getLivingConditionResponses().add(livingConditionService.getEcoFriendlinessByVillageId(village.getId(),status,date));
        villageInfo.setObjectVillageResponses(objectVillageService.getObjectVillageResponses(objectVillageService.getDistinctObjectVillagesByVillageId(village.getId(),status,date)));
        villageInfo.setAnswersQuestionResponses(villageAnswerQuestionService.getAnswersQuestionResponsesByVillageId(village.getId(),status,date));
        villageInfo.setEthnicities(ethnicityVillageService.getUniqueEthnicityVillagesByVillageId(village.getId(), status, date));
        villageInfo.setGroundCategories(villageGroundCategoryService.getUniqueVillageGroundCategoriesByVillageId(village.getId(), status, date));
        return villageInfo;
    }
}
