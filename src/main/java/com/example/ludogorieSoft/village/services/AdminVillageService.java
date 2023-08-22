package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.*;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminVillageService {
    private final VillageRepository villageRepository;
    private final EthnicityVillageService ethnicityVillageService;
    private final VillagePopulationAssertionService villagePopulationAssertionService;
    private final VillageLivingConditionService villageLivingConditionService;
    private final VillageImageService villageImageService;
    private final VillageAnswerQuestionService villageAnswerQuestionService;
    private final ObjectVillageService objectVillageService;
    private final VillageGroundCategoryService villageGroundCategoryService;
    private final VillageService villageService;
    private final ModelMapper modelMapper;

    public List<VillageResponse> getUnapprovedVillageResponsesWithSortedAnswers(boolean status) {
        List<Village> villagesWithAnswers = villageRepository.findAll();
        List<VillageResponse> villageResponses = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String keyWord = "toApprove";
        for (Village village : villagesWithAnswers) {
            VillageResponse villageResponse = createVillageResponse(village, formatter, status, keyWord);
            villageResponses.add(villageResponse);

        }

        return villageResponses;
    }

    public void updateVillageStatusAndVillageResponsesStatus(Long villageId, String answerDate) {
        villageService.increaseApprovedResponsesCount(villageId);
        boolean status = false;
        villagePopulationAssertionService.updateVillagePopulationAssertionStatus(villageId, status, answerDate);
        villageLivingConditionService.updateVillageLivingConditionStatus(villageId, status, answerDate);
        villageImageService.updateVillageImagesStatus(villageId, status, answerDate);
        villageAnswerQuestionService.updateVillageAnswerQuestionStatus(villageId, status, answerDate);
        objectVillageService.updateObjectVillageStatus(villageId, status, answerDate);
        ethnicityVillageService.updateEthnicityVillageStatus(villageId, status, answerDate);
        villageGroundCategoryService.updateVillageGroundCategoryStatus(villageId, status, answerDate);
    }

    public void rejectVillageResponses(Long villageId, String answerDate) {
        LocalDateTime timestamp = TimestampUtils.getCurrentTimestamp();
        boolean status = false;
        VillageDTO villageDTO = villageService.getVillageById(villageId);
        if (villageDTO.getStatus().equals(true)) {
            villageService.updateVillageStatus(villageId, villageDTO);
        }
        villagePopulationAssertionService.rejectVillagePopulationAssertionStatus(villageId, status, answerDate, timestamp);
        villageLivingConditionService.rejectVillageLivingConditionResponse(villageId, status, answerDate, timestamp);
        villageImageService.rejectVillageImages(villageId, status, answerDate, timestamp);
        villageAnswerQuestionService.rejectVillageAnswerQuestionResponse(villageId, status, answerDate, timestamp);
        objectVillageService.rejectObjectVillageResponse(villageId, status, answerDate, timestamp);
        ethnicityVillageService.rejectEthnicityVillageResponse(villageId, status, answerDate, timestamp);
        villageGroundCategoryService.rejectVillageGroundCategoryResponse(villageId, status, answerDate, timestamp);
    }

    public List<VillageResponse> getRejectedVillageResponsesWithSortedAnswers(boolean status) {
        List<Village> villagesWithRejectedResponses = villageRepository.findAllVillagesWithRejectedResponses();
        List<VillageResponse> villageResponses = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String keyWord = "rejected";

        for (Village village : villagesWithRejectedResponses) {
            VillageResponse villageResponse = createVillageResponse(village, formatter, status,keyWord);
            villageResponses.add(villageResponse);
        }

        return villageResponses;
    }

    protected VillageResponse createVillageResponse(Village village, DateTimeFormatter formatter, boolean status, String keyWord) {
        VillageResponse villageResponse = new VillageResponse();
        villageResponse.setId(village.getId());
        villageResponse.setName(village.getName());
        villageResponse.setRegion(village.getRegion());
        villageResponse.setPopulationCount(village.getPopulationCount());
        villageResponse.setStatus(village.getStatus());
        villageResponse.setDateUpload(village.getDateUpload());

        if (village.getAdmin() != null) {
            villageResponse.setAdmin(modelMapper.map(village.getAdmin(), AdministratorDTO.class));
            villageResponse.setDateApproved(village.getDateApproved());
        }
        List<EthnicityVillage> answers = new ArrayList<>();

        if (keyWord.equals("rejected")){
            answers = getRejectedAnswersForVillage(village.getId(), status);
        } else if (keyWord.equals("toApprove")) {
            answers = getAnswersToApprove(village.getId(), status);
        }
        List<String> formattedDates = getFormattedDatesFromAnswers(answers, formatter);

        villageResponse.setGroupedAnswers(Collections.singletonMap(village.getId(), formattedDates));
        return villageResponse;
    }

    protected List<EthnicityVillage> getRejectedAnswersForVillage(Long villageId, boolean status) {
        return ethnicityVillageService.findByVillageIdAndVillageStatusDateDeleteNotNull(villageId, status);
    }
    protected List<EthnicityVillage> getAnswersToApprove(Long villageId, boolean status){
        return ethnicityVillageService.findByVillageIdAndVillageStatus(villageId, status);
    }

    protected List<String> getFormattedDatesFromAnswers(List<EthnicityVillage> answers, DateTimeFormatter formatter) {
        Set<String> uniqueDates = new HashSet<>();
        List<String> formattedDates = new ArrayList<>();

        for (EthnicityVillage answer : answers) {
            String formattedDate = answer.getDateUpload().format(formatter);
            if (uniqueDates.add(formattedDate)) {
                formattedDates.add(formattedDate);
            }
        }

        return formattedDates;
    }

}
