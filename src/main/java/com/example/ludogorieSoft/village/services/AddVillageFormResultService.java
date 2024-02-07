package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.exeptions.NoConsentException;
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

    public AddVillageFormResult create(AddVillageFormResult addVillageFormResult) {
        checkIsImagesHasUserConsent(addVillageFormResult);

        LocalDateTime timestamp = TimestampUtils.getCurrentTimestamp();
        UserDTO userDTO = userService.checkUserDTO(addVillageFormResult.getUserDTO());

        VillageDTO villageDTO = addVillageFormResult.getVillageDTO();
        villageDTO.setRegion(addVillageFormResult.getVillageDTO().getRegion());
        villageDTO.setDateUpload(timestamp);
        VillageDTO savedVillage = villageService.createVillage(villageDTO);

        createPopulationFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);
        createVillageGroundCategoryFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);//ddd
        createEthnicityVillagesFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);//ddd
        createVillageAnswerQuestionsFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);
        createObjectVillagesFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);//ddd
        createVillagePopulationAssertionsFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);
        createVillageLivingConditionFromAddVillageFormResult(savedVillage.getId(), addVillageFormResult, timestamp);
        villageImageService.createImagePaths(addVillageFormResult.getImageBytes(), savedVillage.getId(), timestamp, false, userDTO);
        //villageImageService.createImagePaths2222(addVillageFormResult.getImagesUUID(),savedVillage.getId(), timestamp, false, userDTO);
        return addVillageFormResult;
    }

    public void checkIsImagesHasUserConsent(AddVillageFormResult addVillageFormResult) {

        if (!addVillageFormResult.getImageBytes().isEmpty()
                && (addVillageFormResult.getUserDTO().getFullName().isEmpty()
                || addVillageFormResult.getUserDTO().getEmail().isEmpty()
                || !addVillageFormResult.getUserDTO().getConsent())) {
            throw new NoConsentException("При добавяне на снимки трябва да се съгласите с условията!");
        }

    }

    public PopulationDTO createPopulationFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime) {
        PopulationDTO populationDTO = addVillageFormResult.getPopulationDTO();
        populationDTO.setVillageId(villageId);

        int populationAsNumber = addVillageFormResult.getPopulationDTO().getPopulationCount();
        populationDTO.setNumberOfPopulation(populationService.getNumberOfPopulationByPopulationAsNumber(populationAsNumber));
        populationDTO.setDateUpload(localDateTime);
        populationDTO.setStatus(false);

        populationDTO.setResidents(addVillageFormResult.getPopulationDTO().getResidents());
        populationDTO.setChildren(addVillageFormResult.getPopulationDTO().getChildren());
        populationDTO.setForeigners(addVillageFormResult.getPopulationDTO().getForeigners());

        return populationService.createPopulation(populationDTO);
    }

    public void createVillageGroundCategoryFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime) {
        List<Long> groundCategoryIds = addVillageFormResult.getGroundCategoryIds();
        if (groundCategoryIds == null && !villageGroundCategoryService.existsByVillageIdAndGroundCategoryId(villageId, groundCategoryService.findGroundCategoryByName("не знам").getId())) {
            villageGroundCategoryService.createVillageGroundCategoryDTO(new VillageGroundCategoryDTO(null, villageId, groundCategoryService.findGroundCategoryByName("не знам").getId(), false, localDateTime, null));
        } else if (groundCategoryIds != null) {
            for (Long id : groundCategoryIds) {
                if (!villageGroundCategoryService.existsByVillageIdAndGroundCategoryId(villageId, id)) {
                    VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO(null, villageId, id, false, localDateTime, null);
                    villageGroundCategoryService.createVillageGroundCategoryDTO(villageGroundCategoryDTO);
                }
            }
        }
    }

    public void createEthnicityVillagesFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime) {
        List<Long> ethnicityDTOIds = addVillageFormResult.getEthnicityDTOIds();
        if (ethnicityDTOIds == null && !ethnicityVillageService.existsByVillageIdAndEthnicityId(villageId, ethnicityService.findEthnicityByName("няма малцинствени групи").getId())) {
            ethnicityVillageService.createEthnicityVillage(new EthnicityVillageDTO(null, villageId, ethnicityService.findEthnicityByName("няма малцинствени групи").getId(), false, localDateTime, null));
        } else if (ethnicityDTOIds != null) {
            for (Long id : ethnicityDTOIds) {
                if (!ethnicityVillageService.existsByVillageIdAndEthnicityId(villageId, id)) {
                    EthnicityVillageDTO ethnicityVillageDTO = new EthnicityVillageDTO(null, villageId, id, false, localDateTime, null);
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
                        null, villageId, questionsDTO.get(i).getId(), addVillageFormResult.getQuestionResponses().get(i), false, localDateTime, null);

                villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestionDTO);
            }
        }
    }

    public void createObjectVillagesFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime) {
        List<ObjectVillageDTO> objectVillageDTOS = addVillageFormResult.getObjectVillageDTOS();
        for (int i = 1; i < objectVillageDTOS.size(); i++) {
            ObjectVillageDTO objectVillageToSave = new ObjectVillageDTO(null, villageId, objectVillageDTOS.get(i).getObjectAroundVillageId(), objectVillageDTOS.get(i).getDistance(), false, localDateTime, null);
            if (objectVillageToSave.getDistance() != null) {
                objectVillageService.createObjectVillage(objectVillageToSave);
            }
        }
    }

    public void createVillagePopulationAssertionsFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime) {
        List<VillagePopulationAssertionDTO> villagePopulationAssertionDTOS = addVillageFormResult.getVillagePopulationAssertionDTOS();
        for (int i = 1; i < villagePopulationAssertionDTOS.size(); i++) {
            VillagePopulationAssertionDTO villagePopulationAssertionDTO = new VillagePopulationAssertionDTO(null, villageId, villagePopulationAssertionDTOS.get(i).getPopulatedAssertionId(), villagePopulationAssertionDTOS.get(i).getAnswer(), false, localDateTime, null);
            if (villagePopulationAssertionDTO.getAnswer() != null) {
                villagePopulationAssertionService.createVillagePopulationAssertionDTO(villagePopulationAssertionDTO);
            }
        }
    }

    public void createVillageLivingConditionFromAddVillageFormResult(Long villageId, AddVillageFormResult addVillageFormResult, LocalDateTime localDateTime) {
        List<VillageLivingConditionDTO> villageLivingConditionDTOS = addVillageFormResult.getVillageLivingConditionDTOS();
        for (int i = 1; i < villageLivingConditionDTOS.size(); i++) {
            VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO(null, villageId, villageLivingConditionDTOS.get(i).getLivingConditionId(), villageLivingConditionDTOS.get(i).getConsents(), false, localDateTime, null);
            if (villageLivingConditionDTO.getConsents() != null) {
                villageLivingConditionService.createVillageLivingCondition(villageLivingConditionDTO);
            }
        }
    }
}
