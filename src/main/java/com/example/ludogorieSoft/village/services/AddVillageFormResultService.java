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
    private final GroundCategoryService groundCategoryService;
    private QuestionService questionService;
    private VillageAnswerQuestionService villageAnswerQuestionService;
    private final VillagePopulationAssertionService villagePopulationAssertionService;
    private final ObjectVillageService objectVillageService;
    private VillageLivingConditionService villageLivingConditionService;

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

        List<String> questionResponses = addVillageFormResult.getQuestionResponses();
        List<QuestionDTO> questionsDTO = questionService.getAllQuestions();
        for (int i = 0; i < questionResponses.size(); i++) {
            VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO(null, villageDTO.getId(), questionsDTO.get(i).getId(), addVillageFormResult.getQuestionResponses().get(i));
            if(villageAnswerQuestionDTO.getAnswer() != null){
                villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestionDTO);
            }

        }

        List<ObjectVillageDTO> objectVillageDTOS = addVillageFormResult.getObjectVillageDTOS();
        for (int i = 1; i < objectVillageDTOS.size(); i++) {
            ObjectVillageDTO objectVillageToSave = new ObjectVillageDTO(null, savedVillage.getId(), objectVillageDTOS.get(i).getObjectAroundVillageId(), objectVillageDTOS.get(i).getDistance());
            if(objectVillageToSave.getDistance() != null){
                objectVillageService.createObjectVillage(objectVillageToSave);
            }
        }

        List<VillagePopulationAssertionDTO> villagePopulationAssertionDTOS = addVillageFormResult.getVillagePopulationAssertionDTOS();
        for (int i = 1; i < villagePopulationAssertionDTOS.size(); i++) {
            VillagePopulationAssertionDTO villagePopulationAssertionDTO = new VillagePopulationAssertionDTO(null, savedVillage.getId(), villagePopulationAssertionDTOS.get(i).getPopulatedAssertionId(), villagePopulationAssertionDTOS.get(i).getAnswer());
            if(villagePopulationAssertionDTO.getAnswer() != null){
                villagePopulationAssertionService.createVillagePopulationAssertionDTO(villagePopulationAssertionDTO);
            }
        }

        List<VillageLivingConditionDTO> villageLivingConditionDTOS = addVillageFormResult.getVillageLivingConditionDTOS();
        for (int i = 1; i < villageLivingConditionDTOS.size(); i++) {
            VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO(null, savedVillage.getId(), villageLivingConditionDTOS.get(i).getLivingConditionId(), villageLivingConditionDTOS.get(i).getConsents());
            if(villageLivingConditionDTO.getConsents() != null){
                villageLivingConditionService.createVillageLivingCondition(villageLivingConditionDTO);
            }
        }

        return addVillageFormResult;
    }
}
