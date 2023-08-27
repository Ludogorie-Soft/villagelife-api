package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.exeptions.NoConsentException;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final UserService userService;

    public AddVillageFormResult create(AddVillageFormResult addVillageFormResult){

        checkIsImagesHasUserConsent(addVillageFormResult);

        LocalDateTime timestamp = TimestampUtils.getCurrentTimestamp();
        UserDTO userDTO = userService.checkUserDTO(addVillageFormResult.getUserDTO());

        VillageDTO villageDTO = addVillageFormResult.getVillageDTO();
        PopulationDTO savedPopulation = createPopulationFromAddVillageFormResult(addVillageFormResult);
        villageDTO.setPopulationDTO(savedPopulation);
        villageDTO.setRegion(addVillageFormResult.getVillageDTO().getRegion());
        villageDTO.setDateUpload(timestamp);
        VillageDTO savedVillage = villageService.createVillage(villageDTO);

        createVillageGroundCategoryFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult,timestamp);
        createEthnicityVillagesFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult,timestamp);
        createVillageAnswerQuestionsFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);
        createObjectVillagesFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);
        createVillagePopulationAssertionsFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult,timestamp);
        createVillageLivingConditionFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);
        villageImageService.createImagePaths(addVillageFormResult.getImageBytes(), savedVillage.getId(), timestamp, false, userDTO);
        return addVillageFormResult;
    }
    public void checkIsImagesHasUserConsent(AddVillageFormResult addVillageFormResult){
        if(!addVillageFormResult.getImageBytes().isEmpty()
                && addVillageFormResult.getUserDTO().getFullName().isEmpty()
                && addVillageFormResult.getUserDTO().getEmail().isEmpty()
            && addVillageFormResult.getUserDTO().getConsent().equals(false)){
            throw new NoConsentException("При добавяне на снимки трябва да се съгласите с условията!");
        }

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
    public PopulationDTO createPopulationFromAddVillageFormResult(AddVillageFormResult addVillageFormResult){
        Population existingPopulation = populationService.findPopulationByVillageNameAndRegion(addVillageFormResult.getVillageDTO().getName(), addVillageFormResult.getVillageDTO().getRegion());
        PopulationDTO populationDTO = addVillageFormResult.getPopulationDTO();
        populationDTO.setNumberOfPopulation(getNumberOfPopulationByAddVillageFormResult(addVillageFormResult));
        if(existingPopulation == null){
            return populationService.createPopulation(populationDTO);
        }else {
            return populationService.updatePopulation(existingPopulation.getId(), populationDTO);
        }
    }
    public void createVillageGroundCategoryFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime) {
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
            villageGroundCategoryDTO.setStatus(false);
            villageGroundCategoryDTO.setDateUpload(localDateTime);
            villageGroundCategoryService.createVillageGroundCategoryDTO(villageGroundCategoryDTO);
        }
    }
    public void createEthnicityVillagesFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime){
        List<Long> ethnicityDTOIds = addVillageFormResult.getEthnicityDTOIds();
        if (ethnicityDTOIds == null && !ethnicityVillageService.existsByVillageIdAndEthnicityId(villageId, ethnicityService.findEthnicityByName("няма малцинствени групи").getId())) {
            ethnicityVillageService.createEthnicityVillage(new EthnicityVillageDTO(null, villageId, ethnicityService.findEthnicityByName("няма малцинствени групи").getId(),false,localDateTime,null));
        }else if(ethnicityDTOIds != null){
            for (Long id : ethnicityDTOIds) {
                if(!ethnicityVillageService.existsByVillageIdAndEthnicityId(villageId, id)){
                    EthnicityVillageDTO ethnicityVillageDTO = new EthnicityVillageDTO(null, villageId, id,false,localDateTime,null);
                    ethnicityVillageService.createEthnicityVillage(ethnicityVillageDTO);
                }
            }
        }
    }
    public void createVillageAnswerQuestionsFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime) {
        List<String> questionResponses = addVillageFormResult.getQuestionResponses();
        List<QuestionDTO> questionsDTO = questionService.getAllQuestions();
        for (int i = 0; i < questionResponses.size(); i++) {
            if (!addVillageFormResult.getQuestionResponses().get(i).equals("") &&
                    !villageAnswerQuestionService.existsByVillageIdAndQuestionIdAndAnswer(
                            villageId, questionsDTO.get(i).getId(), addVillageFormResult.getQuestionResponses().get(i))) {

                VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO(
                        null, villageId, questionsDTO.get(i).getId(), addVillageFormResult.getQuestionResponses().get(i),false,localDateTime,null);

                villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestionDTO);
            }
        }
    }
    public void createObjectVillagesFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime){
        List<ObjectVillageDTO> objectVillageDTOS = addVillageFormResult.getObjectVillageDTOS();
        for (int i = 1; i < objectVillageDTOS.size(); i++) {
            ObjectVillageDTO objectVillageToSave = new ObjectVillageDTO(null, villageId, objectVillageDTOS.get(i).getObjectAroundVillageId(), objectVillageDTOS.get(i).getDistance(),false,localDateTime,null);
            if(objectVillageToSave.getDistance() != null){
                objectVillageService.createObjectVillage(objectVillageToSave);
            }
        }
    }
    public void createVillagePopulationAssertionsFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime){
        List<VillagePopulationAssertionDTO> villagePopulationAssertionDTOS = addVillageFormResult.getVillagePopulationAssertionDTOS();
        for (int i = 1; i < villagePopulationAssertionDTOS.size(); i++) {
            VillagePopulationAssertionDTO villagePopulationAssertionDTO = new VillagePopulationAssertionDTO(null, villageId, villagePopulationAssertionDTOS.get(i).getPopulatedAssertionId(), villagePopulationAssertionDTOS.get(i).getAnswer(),false,localDateTime,null);
            if(villagePopulationAssertionDTO.getAnswer() != null){
                villagePopulationAssertionService.createVillagePopulationAssertionDTO(villagePopulationAssertionDTO);
            }
        }
    }
    public void createVillageLivingConditionFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime){
        List<VillageLivingConditionDTO> villageLivingConditionDTOS = addVillageFormResult.getVillageLivingConditionDTOS();
        for (int i = 1; i < villageLivingConditionDTOS.size(); i++) {
            VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO(null, villageId, villageLivingConditionDTOS.get(i).getLivingConditionId(), villageLivingConditionDTOS.get(i).getConsents(),false, localDateTime,null);
            if(villageLivingConditionDTO.getConsents() != null){
                villageLivingConditionService.createVillageLivingCondition(villageLivingConditionDTO);
            }
        }
    }
}
