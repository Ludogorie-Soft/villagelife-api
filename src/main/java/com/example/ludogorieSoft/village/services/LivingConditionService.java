package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.LivingConditionDTO;
import com.example.ludogoriesoft.village.model.LivingCondition;
import com.example.ludogoriesoft.village.repositories.LivingConditionRepository;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LivingConditionService {
    private final LivingConditionRepository livingConditionsRepository;
    private final ModelMapper modelMapper;

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
        if (livingCondition.isPresent()){
            return livingCondition.get();
        }else {
            throw new ApiRequestException("Living Condition not found");
        }
    }
}
