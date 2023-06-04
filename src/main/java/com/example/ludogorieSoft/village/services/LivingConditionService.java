package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.model.LivingCondition;
import com.example.ludogorieSoft.village.repositories.LivingConditionRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
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
        if (livingConditionsRepository.existsByLivingCondition(livingConditionDTO.getLivingCondition())) {
            throw new ApiRequestException("LivingCondition with condition: " + livingConditionDTO.getLivingCondition() + " already exists");
        }
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setLivingCondition(livingConditionDTO.getLivingCondition());
        livingConditionsRepository.save(livingCondition);
        return livingConditionDTO;
    }

    public LivingConditionDTO updateLivingCondition(Long id, LivingCondition livingCondition) {
        Optional<LivingCondition> foundLivingCondition = livingConditionsRepository.findById(id);
        if (foundLivingCondition.isEmpty()) {
            throw new ApiRequestException("LivingCondition with id: " + id + " Not Found");
        }
        if (livingConditionsRepository.existsByLivingCondition(livingCondition.getLivingCondition())) {
            throw new ApiRequestException("LivingCondition with name: " + livingCondition.getLivingCondition() + " already exists");
        }
        foundLivingCondition.get().setLivingCondition(livingCondition.getLivingCondition());

        livingConditionsRepository.save(foundLivingCondition.get());
        return livingConditionToLivingConditionDTO(foundLivingCondition.get());
    }

    public void deleteLivingCondition(Long id) {
        Optional<LivingCondition> livingCondition = livingConditionsRepository.findById(id);
        if (livingCondition.isEmpty()) {
            throw new ApiRequestException("LivingCondition not found for id " + id);
        }
        livingConditionsRepository.delete(livingCondition.get());
    }


}
