package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminVillageService {
    private final VillageRepository villageRepository;
    private final EthnicityVillageService ethnicityVillageService;
    private final ModelMapper modelMapper;

    public List<VillageResponse> getUnapprovedVillageResponsesWithSortedAnswers(boolean status) {
        List<Village> villagesWithAnswers = villageRepository.findAll();
        List<VillageResponse> villageResponses = new ArrayList<>();

        for (Village village : villagesWithAnswers) {
            List<EthnicityVillage> answers = ethnicityVillageService.findByVillageIdAndVillageStatus(village.getId(), status);
            answers.sort(Comparator.comparing(EthnicityVillage::getDateUpload)); // Sort answers by date

            Map<LocalDateTime, Boolean> villageAnswersMap = new HashMap<>();
            for (EthnicityVillage answer : answers) {
                villageAnswersMap.put(answer.getDateUpload(), false);
            }

            VillageResponse villageResponse = new VillageResponse();
            villageResponse.setId(village.getId());
            villageResponse.setName(village.getName());
            villageResponse.setRegion(village.getRegion());
            villageResponse.setPopulationCount(village.getPopulationCount());
            villageResponse.setDateUpload(village.getDateUpload());
            villageResponse.setStatus(village.getStatus());
            if (village.getAdmin() != null) {
                villageResponse.setAdmin(modelMapper.map(village.getAdmin(),AdministratorDTO.class));
                villageResponse.setDateApproved(village.getDateApproved());
            } else {
                villageResponse.setAdmin(null);
                villageResponse.setDateApproved(null);
            }
            villageResponse.setGroupedAnswers(Collections.singletonMap(village.getId(), villageAnswersMap));
            villageResponses.add(villageResponse);
        }

        return villageResponses;
    }

}
