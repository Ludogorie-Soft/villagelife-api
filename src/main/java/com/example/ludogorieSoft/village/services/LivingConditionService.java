package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.dtos.response.LivingConditionResponse;
import com.example.ludogorieSoft.village.model.LivingCondition;
import com.example.ludogorieSoft.village.model.ObjectVillage;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import com.example.ludogorieSoft.village.repositories.LivingConditionRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.repositories.ObjectVillageRepository;
import com.example.ludogorieSoft.village.repositories.VillageLivingConditionRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LivingConditionService {
    private final LivingConditionRepository livingConditionsRepository;
    private final ModelMapper modelMapper;
    private final VillageLivingConditionRepository villageLivingConditionRepository;
    private final ObjectVillageRepository objectVillageRepository;
    private final ObjectAroundVillageService objectAroundVillageService;

    public LivingConditionDTO livingConditionToLivingConditionDTO(LivingCondition livingCondition) {
        return modelMapper.map(livingCondition, LivingConditionDTO.class);
    }

    public List<LivingConditionDTO> getAllLivingConditions() {
        List<LivingCondition> livingConditionList = livingConditionsRepository.findAll();
        return livingConditionList
                .stream()
                .map(this::livingConditionToLivingConditionDTO)
                .toList();
    }

    public LivingConditionDTO getLivingConditionById(Long id) {
        Optional<LivingCondition> optionalLivingCondition = livingConditionsRepository.findById(id);
        if (optionalLivingCondition.isEmpty()) {
            throw new ApiRequestException("LivingCondition with id: " + id + " Not Found");
        }
        return livingConditionToLivingConditionDTO(optionalLivingCondition.get());
    }


    public LivingConditionDTO createLivingCondition(LivingConditionDTO livingConditionDTO) {
        if (StringUtils.isBlank(livingConditionDTO.getLivingConditionName())) {
            throw new ApiRequestException("Living Condition is blank");
        }
        if (livingConditionsRepository.existsByLivingConditionName(livingConditionDTO.getLivingConditionName())) {
            throw new ApiRequestException("Living Condition with name: " + livingConditionDTO.getLivingConditionName() + " already exists");
        }
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setLivingConditionName(livingConditionDTO.getLivingConditionName());
        livingConditionsRepository.save(livingCondition);
        return livingConditionDTO;
    }

    public LivingConditionDTO updateLivingCondition(Long id, LivingConditionDTO livingConditionDTO) {
        Optional<LivingCondition> foundLivingCondition = livingConditionsRepository.findById(id);
        LivingCondition livingCondition = foundLivingCondition.orElseThrow(() -> new ApiRequestException("Living condition not found"));

        if (livingConditionDTO == null || livingConditionDTO.getLivingConditionName() == null || livingConditionDTO.getLivingConditionName().isEmpty()) {
            throw new ApiRequestException("Invalid Living Condition data");
        }

        String newLivingConditionName = livingConditionDTO.getLivingConditionName();
        if (!newLivingConditionName.equals(livingCondition.getLivingConditionName())) {
            if (livingConditionsRepository.existsByLivingConditionName(newLivingConditionName)) {
                throw new ApiRequestException("Living Condition with name: " + newLivingConditionName + " already exists");
            }
            livingCondition.setLivingConditionName(newLivingConditionName);
            livingConditionsRepository.save(livingCondition);
        }

        return livingConditionToLivingConditionDTO(livingCondition);
    }


    public void deleteLivingCondition(Long id) {
        Optional<LivingCondition> livingCondition = livingConditionsRepository.findById(id);
        if (livingCondition.isEmpty()) {
            throw new ApiRequestException("Living Condition not found for id " + id);
        }
        livingConditionsRepository.delete(livingCondition.get());
    }


    public LivingCondition checkLivingCondition(Long id) {
        Optional<LivingCondition> livingCondition = livingConditionsRepository.findById(id);
        if (livingCondition.isPresent()) {
            return livingCondition.get();
        } else {
            throw new ApiRequestException("Living Condition not found");
        }
    }

    public List<LivingConditionResponse> getLivingConditionResponses(Long villageId, boolean status, String date) {
        List<String> names = new ArrayList<>(Arrays.asList("infrastructure", "public.transport", "power.supply", "water.supply", "mobile.range", "internet", "tv", "cleanliness"));
        List<Long> livingConditionIds = new ArrayList<>(Arrays.asList(1L, 3L, 4L, 5L, 6L, 7L, 8L, 9L));
        List<LivingConditionResponse> livingConditionResponses = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            if (status && !villageLivingConditionRepository.existsByVillageIdAndLivingConditionIdAndVillageStatus(villageId, livingConditionIds.get(i), true)) {
                continue;
            }
            if (!status && !villageLivingConditionRepository.existsByVillageIdAndLivingConditionIdAndVillageStatusAndDate(villageId, livingConditionIds.get(i), false, date)) {
                continue;
            }
            LivingConditionResponse livingConditionResponse = new LivingConditionResponse();
            livingConditionResponse.setLivingCondition(names.get(i));
            double percentage = getPercentage(villageId, livingConditionIds.get(i), status, date);
            livingConditionResponse.setPercentage(Math.round(percentage * 100.0) / 100.0);
            livingConditionResponses.add(livingConditionResponse);
        }
        return livingConditionResponses;
    }

    public LivingConditionResponse getAccessibilityByVillageId(Long villageId, boolean status, String date) {
        LivingConditionResponse livingConditionResponse = new LivingConditionResponse();
        livingConditionResponse.setLivingCondition("accessibility");

        double accessibleInWinterPercentage = getPercentage(villageId, 2L, status, date);
        double easilyAccessiblePercentage = getPercentage(villageId, 11L, status, date);

        livingConditionResponse.setPercentage(Math.round((accessibleInWinterPercentage + easilyAccessiblePercentage) / 2 * 100.0) / 100.0);
        return livingConditionResponse;
    }

    public LivingConditionResponse getCrimeByVillageId(Long villageId, boolean status, String date) {
        LivingConditionResponse livingConditionResponse = new LivingConditionResponse();
        livingConditionResponse.setLivingCondition("crime");

        double crimePercentage = getPercentage(villageId, 10L, status, date);
        livingConditionResponse.setPercentage(Math.round((100 - crimePercentage) * 100.0) / 100.0);
        return livingConditionResponse;
    }

    public double getLivingConditionsMainPercentage(Long villageId, boolean status, String date) {
        List<Long> conditionIds = new ArrayList<>(Arrays.asList(1L, 3L, 4L, 5L, 6L, 7L, 8L, 9L));
        int count = 0;
        double finalPercentage = 0;
        for (Long conditionId : conditionIds) {
            if (!villageLivingConditionRepository.existsByVillageIdAndLivingConditionIdAndVillageStatus(villageId, conditionId, true) && status) {
                continue;
            }

            if (!villageLivingConditionRepository.existsByVillageIdAndLivingConditionIdAndVillageStatusAndDate(villageId, conditionId, status, date) && !status) {
                continue;
            }
            double percentage = 0;
            List<VillageLivingConditions> villageLivingConditions;
            if (status) {
                villageLivingConditions = villageLivingConditionRepository.findByVillageIdAndLivingConditionIdAndVillageStatus(villageId, conditionId, true);
            } else {
                villageLivingConditions = villageLivingConditionRepository.findByVillageIdAndLivingConditionIdAndVillageStatusAndDate(villageId, conditionId, status, date);
            }
            for (VillageLivingConditions villageLivingCondition : villageLivingConditions) {
                percentage += villageLivingCondition.getConsents().getValue();
            }
            count++;
            finalPercentage += percentage / villageLivingConditions.size();
        }
        if (count != 0) {
            return finalPercentage / count;
        } else {
            return 0;
        }
    }

    public double getLivingConditionsForumPercentage(Long villageId, boolean status, String date) {
        List<ObjectAroundVillageDTO> objectAroundVillageDTOS = objectAroundVillageService.getAllObjectsAroundVillage();
        double finalPercentage = 0;
        double count = 0;
        for (ObjectAroundVillageDTO object : objectAroundVillageDTOS) {
            if (!objectVillageRepository.existsByVillageIdAndObjectIdAndVillageStatus(villageId, object.getId(), true) && status) {
                continue;
            }
            if (!objectVillageRepository.existsByVillageIdAndObjectIdAndVillageStatusAndDate(villageId, object.getId(), status, date) && !status) {
                continue;
            }
            double percentage = 0;
            List<ObjectVillage> objectVillages;
            if (status) {
                objectVillages = objectVillageRepository.findByVillageIdAndObjectIdAndVillageStatus(villageId, object.getId(), true);
            } else {
                objectVillages = objectVillageRepository.findByVillageIdAndObjectIdAndVillageStatusAndDate(villageId, object.getId(), status, date);
            }
            for (ObjectVillage objectVillage : objectVillages) {
                percentage += objectVillage.getDistance().getValue();
            }
            count++;
            finalPercentage += percentage / objectVillages.size();
        }
        if (count != 0) {
            return finalPercentage / count;
        } else {
            return 0;
        }
    }

    public LivingConditionResponse getTotalLivingConditionsByVillageId(Long villageId, boolean status, String date) {
        LivingConditionResponse livingConditionResponse = new LivingConditionResponse();
        livingConditionResponse.setLivingCondition("living.conditions.label");
        double percentage = (getLivingConditionsMainPercentage(villageId, status, date) + getLivingConditionsForumPercentage(villageId, status, date)) / 2;
        livingConditionResponse.setPercentage(Math.round((percentage) * 100.0) / 100.0);
        return livingConditionResponse;
    }

    public LivingConditionResponse getEcoFriendlinessByVillageId(Long villageId, boolean status, String date) {
        LivingConditionResponse livingConditionResponse = new LivingConditionResponse();
        livingConditionResponse.setLivingCondition("environmental.friendliness");

        double natureReservePercentage = getPercentage(villageId, 12L, status, date);
        double environmentallyFriendlyLivingPercentage = getPercentage(villageId, 13L, status, date);
        livingConditionResponse.setPercentage(Math.round((natureReservePercentage + environmentallyFriendlyLivingPercentage) / 2 * 100.0) / 100.0);

        return livingConditionResponse;
    }

    public double getPercentage(Long villageId, Long conditionId, boolean status, String date) {
        List<VillageLivingConditions> villageLivingConditions;
        if (status) {
            villageLivingConditions = villageLivingConditionRepository.findByVillageIdAndLivingConditionIdAndVillageStatus(villageId, conditionId, status);
        } else {
            villageLivingConditions = villageLivingConditionRepository.findByVillageIdAndLivingConditionIdAndVillageStatusAndDate(villageId, conditionId, status, date);
        }
        double percentage = 0;
        for (VillageLivingConditions villageLivingCondition : villageLivingConditions) {
            percentage += villageLivingCondition.getConsents().getValue();
        }
        return percentage / villageLivingConditions.size();
    }
}
