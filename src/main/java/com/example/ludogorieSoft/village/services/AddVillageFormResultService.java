package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
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
        populationDTO.setNumberOfPopulation(getNumberOfPopulationByAddVillageFormResult(addVillageFormResult));
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
    public NumberOfPopulation getNumberOfPopulationByAddVillageFormResult(AddVillageFormResult addVillageFormResult){
        int populationAsNumber = addVillageFormResult.getVillageDTO().getPopulationCount();
        if(populationAsNumber <= 10){
            return NumberOfPopulation.UP_TO_10_PEOPLE;
        }else if(populationAsNumber <= 50){
            return NumberOfPopulation.FROM_11_TO_50_PEOPLE;
        }else if(populationAsNumber <= 200){
            return NumberOfPopulation.FROM_51_TO_200_PEOPLE;
        }else if(populationAsNumber <= 500){
            return NumberOfPopulation.FROM_201_TO_500_PEOPLE;
        }else if (populationAsNumber <= 1000){
            return NumberOfPopulation.FROM_501_TO_1000_PEOPLE;
        }else if(populationAsNumber <= 2000){
            return NumberOfPopulation.FROM_1001_TO_2000_PEOPLE;
        }else {
            return NumberOfPopulation.FROM_2000_PEOPLE;
        }
    }
    public void createVillageGroundCategoryFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult) {
        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        GroundCategoryDTO groundCategoryDTO = groundCategoryService.getByGroundCategoryName(addVillageFormResult.getGroundCategoryName());
        villageGroundCategoryDTO.setGroundCategoryId(groundCategoryDTO.getId());
        try{
            villageGroundCategoryDTO = villageGroundCategoryService.findVillageGroundCategoryDTOByVillageId(villageId);
            villageGroundCategoryDTO.setGroundCategoryId(groundCategoryDTO.getId());
            villageGroundCategoryService.updateVillageGroundCategory(villageGroundCategoryDTO.getId(), villageGroundCategoryDTO);
        }catch (Exception e){
            villageGroundCategoryDTO.setVillageId(villageId);
            villageGroundCategoryDTO.setGroundCategoryId(groundCategoryDTO.getId());
            villageGroundCategoryService.createVillageGroundCategoryDTO(villageGroundCategoryDTO);
        }
    }
    public void createEthnicityVillagesFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult){
        List<Long> ethnicityDTOIds = addVillageFormResult.getEthnicityDTOIds();
        if (ethnicityDTOIds == null && !ethnicityVillageService.existsByVillageIdAndEthnicityId(villageId, ethnicityService.findEthnicityByName("няма малцинствени групи").getId())) {
            ethnicityVillageService.createEthnicityVillage(new EthnicityVillageDTO(null, villageId, ethnicityService.findEthnicityByName("няма малцинствени групи").getId()));
        }else if(ethnicityDTOIds != null){
            for (Long id : ethnicityDTOIds) {
                if(!ethnicityVillageService.existsByVillageIdAndEthnicityId(villageId, id)){
                    EthnicityVillageDTO ethnicityVillageDTO = new EthnicityVillageDTO(null, villageId, id);
                    ethnicityVillageService.createEthnicityVillage(ethnicityVillageDTO);
                }
            }
        }
    }
    public void createVillageAnswerQuestionsFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult) {
        List<String> questionResponses = addVillageFormResult.getQuestionResponses();
        List<QuestionDTO> questionsDTO = questionService.getAllQuestions();
        for (int i = 0; i < questionResponses.size(); i++) {
            if (!addVillageFormResult.getQuestionResponses().get(i).equals("") &&
                    !villageAnswerQuestionService.existsByVillageIdAndQuestionIdAndAnswer(
                            villageId, questionsDTO.get(i).getId(), addVillageFormResult.getQuestionResponses().get(i))) {

                VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO(
                        null, villageId, questionsDTO.get(i).getId(), addVillageFormResult.getQuestionResponses().get(i));

                villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestionDTO);
            }
        }
    }
    public void createObjectVillagesFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult){
        List<ObjectVillageDTO> objectVillageDTOS = addVillageFormResult.getObjectVillageDTOS();
        for (int i = 1; i < objectVillageDTOS.size(); i++) {
            ObjectVillageDTO objectVillageToSave = new ObjectVillageDTO(null, villageId, objectVillageDTOS.get(i).getObjectAroundVillageId(), objectVillageDTOS.get(i).getDistance());
            if(objectVillageToSave.getDistance() != null && !objectVillageService.existsByVillageIdAndObjectIdAndDistance(villageId, objectVillageDTOS.get(i).getObjectAroundVillageId(), objectVillageDTOS.get(i).getDistance())){
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
