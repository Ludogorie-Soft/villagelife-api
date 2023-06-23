package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.*;
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
    private EthnicityService ethnicityService;
    private VillageImageService villageImageService;

    public AddVillageFormResult create(AddVillageFormResult addVillageFormResult){
        PopulationDTO populationDTO = addVillageFormResult.getPopulationDTO();
        PopulationDTO savedPopulation = populationService.createPopulation(populationDTO);

        VillageDTO villageDTO = addVillageFormResult.getVillageDTO();
        villageDTO.setPopulationDTO(savedPopulation);
        villageDTO.setRegion(addVillageFormResult.getVillageDTO().getRegion());
        VillageDTO savedVillage = villageService.createVillage(villageDTO);

        createVillageGroundCategoryFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult);
        createEthnicityVillagesFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult);
        createVillageAnswerQuestionsFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult);
        createObjectVillagesFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult);
        createVillagePopulationAssertionsFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult);
        createVillageLivingConditionFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult);

        villageImageService.createImagePaths(addVillageFormResult.getImageBytes(), savedVillage.getId());
        return addVillageFormResult;
    }
    public void createVillageGroundCategoryFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult) {
        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO.setVillageId(villageId);
        GroundCategoryDTO groundCategoryDTO = groundCategoryService.getByGroundCategoryName(addVillageFormResult.getGroundCategoryName());
        villageGroundCategoryDTO.setGroundCategoryId(groundCategoryDTO.getId());
        villageGroundCategoryService.createVillageGroundCategoryDTO(villageGroundCategoryDTO);
    }
    public void createEthnicityVillagesFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult){
        List<Long> ethnicityDTOIds = addVillageFormResult.getEthnicityDTOIds();
        if (ethnicityDTOIds == null) {
            ethnicityVillageService.createEthnicityVillage(new EthnicityVillageDTO(null, villageId, ethnicityService.findEthnicityByName("няма малцинствени групи").getId()));
            return;
        }
        for (Long id : ethnicityDTOIds) {
            EthnicityVillageDTO ethnicityVillageDTO = new EthnicityVillageDTO(null, villageId, id);
            ethnicityVillageService.createEthnicityVillage(ethnicityVillageDTO);
        }
    }
    public void createVillageAnswerQuestionsFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult){
        List<String> questionResponses = addVillageFormResult.getQuestionResponses();
        List<QuestionDTO> questionsDTO = questionService.getAllQuestions();
        for (int i = 0; i < questionResponses.size(); i++) {
            VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO(null, villageId, questionsDTO.get(i).getId(), addVillageFormResult.getQuestionResponses().get(i));
            if(villageAnswerQuestionDTO.getAnswer() != null){
                villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestionDTO);
            }

        }
    }
    public void createObjectVillagesFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult){
        List<ObjectVillageDTO> objectVillageDTOS = addVillageFormResult.getObjectVillageDTOS();
        for (int i = 1; i < objectVillageDTOS.size(); i++) {
            ObjectVillageDTO objectVillageToSave = new ObjectVillageDTO(null, villageId, objectVillageDTOS.get(i).getObjectAroundVillageId(), objectVillageDTOS.get(i).getDistance());
            if(objectVillageToSave.getDistance() != null){
                objectVillageService.createObjectVillage(objectVillageToSave);
            }
        }
    }
    public void createVillagePopulationAssertionsFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult){
        List<VillagePopulationAssertionDTO> villagePopulationAssertionDTOS = addVillageFormResult.getVillagePopulationAssertionDTOS();
        for (int i = 1; i < villagePopulationAssertionDTOS.size(); i++) {
            VillagePopulationAssertionDTO villagePopulationAssertionDTO = new VillagePopulationAssertionDTO(null, villageId, villagePopulationAssertionDTOS.get(i).getPopulatedAssertionId(), villagePopulationAssertionDTOS.get(i).getAnswer());
            if(villagePopulationAssertionDTO.getAnswer() != null){
                villagePopulationAssertionService.createVillagePopulationAssertionDTO(villagePopulationAssertionDTO);
            }
        }
    }
    public void createVillageLivingConditionFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult){
        List<VillageLivingConditionDTO> villageLivingConditionDTOS = addVillageFormResult.getVillageLivingConditionDTOS();
        for (int i = 1; i < villageLivingConditionDTOS.size(); i++) {
            VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO(null, villageId, villageLivingConditionDTOS.get(i).getLivingConditionId(), villageLivingConditionDTOS.get(i).getConsents());
            if(villageLivingConditionDTO.getConsents() != null){
                villageLivingConditionService.createVillageLivingCondition(villageLivingConditionDTO);
            }
        }
    }
}
