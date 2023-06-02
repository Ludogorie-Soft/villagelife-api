package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.LivingConditionsDTO;
import com.example.ludogorieSoft.village.model.LivingConditions;
import com.example.ludogorieSoft.village.repositories.LivingConditionsRepositories;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LivingConditionService {
    private final LivingConditionsRepositories livingConditionsRepository;
    private final ModelMapper modelMapper;

    public LivingConditionsDTO livingConditionToLivingConditionDTO(LivingConditions livingCondition) {
        return modelMapper.map(livingCondition, LivingConditionsDTO.class);
    }

    public List<LivingConditionsDTO> getAllLivingConditions() {
        List<LivingConditions> livingConditionsList = livingConditionsRepository.findAll();
        return livingConditionsList
                .stream()
                .map(this::livingConditionToLivingConditionDTO)
                .collect(Collectors.toList());
    }

    public LivingConditionsDTO getLivingConditionById(Long id) {
        Optional<LivingConditions> optionalLivingCondition = livingConditionsRepository.findById(id);
        if (optionalLivingCondition.isEmpty()) {
            throw new ApiRequestException("LivingCondition with id: " + id + " Not Found");
        }
        return livingConditionToLivingConditionDTO(optionalLivingCondition.get());
    }

    public LivingConditionsDTO createLivingCondition(LivingConditionsDTO livingConditionDTO) {
        if (livingConditionsRepository.existsByLivingCondition(livingConditionDTO.getLivingCondition())) {
            throw new ApiRequestException("LivingCondition with condition: " + livingConditionDTO.getLivingCondition() + " already exists");
        }

        LivingConditions livingCondition = new LivingConditions();
        livingCondition.setLivingCondition(livingConditionDTO.getLivingCondition());
        livingConditionsRepository.save(livingCondition);

        return livingConditionDTO;
    }

    public LivingConditionsDTO updateLivingCondition(Long id, LivingConditions livingCondition) {
        Optional<LivingConditions> foundLivingCondition = livingConditionsRepository.findById(id);
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
        Optional<LivingConditions> livingCondition = livingConditionsRepository.findById(id);
        if (livingCondition.isEmpty()) {
            throw new ApiRequestException("LivingCondition not found for id " + id);
        }
        livingConditionsRepository.delete(livingCondition.get());
    }


}
