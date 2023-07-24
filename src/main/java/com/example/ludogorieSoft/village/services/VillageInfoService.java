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
    public VillageInfo getVillageInfoByVillageId(Long villageId){
        Village village = villageService.checkVillage(villageId);
        VillageInfo villageInfo = new VillageInfo();
        villageInfo.setVillageDTO(villageService.getVillageById(village.getId()));

        villageInfo.setPopulationAssertionResponses(populatedAssertionService.getPopulationAssertionResponse(village.getId()));
        villageInfo.setLivingConditionResponses(livingConditionService.getLivingConditionResponses(village.getId()));
        villageInfo.getLivingConditionResponses().add(livingConditionService.getAccessibilityByVillageId(village.getId()));
        villageInfo.getLivingConditionResponses().add(livingConditionService.getCrimeByVillageId(village.getId()));
        villageInfo.getLivingConditionResponses().add(livingConditionService.getTotalLivingConditionsByVillageId(village.getId()));
        villageInfo.getLivingConditionResponses().add(livingConditionService.getEcoFriendlinessByVillageId(village.getId()));
        villageInfo.setObjectVillages(objectVillageService.getDistinctObjectVillagesByVillageId(villageId));
        villageInfo.setVillageAnswerQuestionDTOs(villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(village.getId()));
        villageInfo.setEthnicityVillageDTOS(ethnicityVillageService.getUniqueEthnicityVillagesByVillageId(village.getId()));

        return villageInfo;
    }
}
