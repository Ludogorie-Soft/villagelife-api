package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminVillageService {
    private final VillageRepository villageRepository;
    private final EthnicityVillageService ethnicityVillageService;
    private final ModelMapper modelMapper;

//    public List<VillageResponse> getUnapprovedVillageResponsesWithSortedAnswers(boolean status) { last workin with date and status
//        List<Village> villagesWithAnswers = villageRepository.findAll();
//        List<VillageResponse> villageResponses = new ArrayList<>();
//
//        for (Village village : villagesWithAnswers) {
//            List<EthnicityVillage> answers = ethnicityVillageService.findByVillageIdAndVillageStatus(village.getId(), status);
//            answers.sort(Comparator.comparing(EthnicityVillage::getDateUpload)); // Sort answers by date
//
//            Map<LocalDateTime, Boolean> villageAnswersMap = new HashMap<>();
//            System.out.println("metod 1 " + villageAnswersMap);
//            for (EthnicityVillage answer : answers) {
//                villageAnswersMap.put(answer.getDateUpload(), answer.getVillageStatus());
//            }
//
//            VillageResponse villageResponse = new VillageResponse();
//            villageResponse.setId(village.getId());
//            villageResponse.setName(village.getName());
//            villageResponse.setRegion(village.getRegion());
//            villageResponse.setPopulationCount(village.getPopulationCount());
//            villageResponse.setDateUpload(village.getDateUpload());
//            villageResponse.setStatus(village.getStatus());
//            if (village.getAdmin() != null) {
//                villageResponse.setAdmin(modelMapper.map(village.getAdmin(),AdministratorDTO.class));
//                villageResponse.setDateApproved(village.getDateApproved());
//            } else {
//                villageResponse.setAdmin(null);
//                villageResponse.setDateApproved(null);
//            }
//            villageResponse.setGroupedAnswers(Collections.singletonMap(village.getId(), villageAnswersMap));
//            System.out.println("metod 2 " + Collections.singletonMap(village.getId(), villageAnswersMap));
//            villageResponses.add(villageResponse);
//        }
//
//        return villageResponses;
//    }

//    public List<VillageResponse> getUnapprovedVillageResponsesWithSortedAnswers(boolean status) {
//        List<Village> villagesWithAnswers = villageRepository.findAll();
//        List<VillageResponse> villageResponses = new ArrayList<>();
//
//        // Create a DateTimeFormatter for the desired date format
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        for (Village village : villagesWithAnswers) {
//            List<EthnicityVillage> answers = ethnicityVillageService.findByVillageIdAndVillageStatus(village.getId(), status);
//            answers.sort(Comparator.comparing(EthnicityVillage::getDateUpload)); // Sort answers by date
//
//            List<String> formattedDates = answers.stream()
//                    .map(answer -> answer.getDateUpload().format(formatter))
//                    .collect(Collectors.toList());
//
//            VillageResponse villageResponse = new VillageResponse();
//            villageResponse.setId(village.getId());
//            villageResponse.setName(village.getName());
//            villageResponse.setRegion(village.getRegion());
//            villageResponse.setPopulationCount(village.getPopulationCount());
//            villageResponse.setDateUpload(village.getDateUpload());
//            villageResponse.setStatus(village.getStatus());
//            if (village.getAdmin() != null) {
//                villageResponse.setAdmin(modelMapper.map(village.getAdmin(), AdministratorDTO.class));
//                villageResponse.setDateApproved(village.getDateApproved());
//            } else {
//                villageResponse.setAdmin(null);
//                villageResponse.setDateApproved(null);
//            }
//
//            // Add answers for this village to the groupedAnswers map
//            villageResponse.setGroupedAnswers(Collections.singletonMap(village.getId(), formattedDates));
//            villageResponses.add(villageResponse);
//        }
//
//        return villageResponses;
//    }
public List<VillageResponse> getUnapprovedVillageResponsesWithSortedAnswers(boolean status) {
    List<Village> villagesWithAnswers = villageRepository.findAll();
    List<VillageResponse> villageResponses = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    for (Village village : villagesWithAnswers) {
        List<EthnicityVillage> answers = ethnicityVillageService.findByVillageIdAndVillageStatus(village.getId(), status);
        answers.sort(Comparator.comparing(EthnicityVillage::getDateUpload)); // Sort answers by date

        Set<String> uniqueDates = new HashSet<>();
        List<String> formattedDates = new ArrayList<>();

        for (EthnicityVillage answer : answers) {
            String formattedDate = answer.getDateUpload().truncatedTo(ChronoUnit.SECONDS).format(formatter);
            if (uniqueDates.add(formattedDate)) {
                formattedDates.add(formattedDate);
            }
        }

        VillageResponse villageResponse = new VillageResponse();
        villageResponse.setId(village.getId());
        villageResponse.setName(village.getName());
        villageResponse.setRegion(village.getRegion());
        villageResponse.setPopulationCount(village.getPopulationCount());
        villageResponse.setDateUpload(village.getDateUpload());
        villageResponse.setAdmin(village.getAdmin() != null ? modelMapper.map(village.getAdmin(), AdministratorDTO.class) : null);
        villageResponse.setDateApproved(village.getDateApproved());
        villageResponse.setGroupedAnswers(Collections.singletonMap(village.getId(), formattedDates));
        villageResponses.add(villageResponse);
    }

    return villageResponses;
}

}
