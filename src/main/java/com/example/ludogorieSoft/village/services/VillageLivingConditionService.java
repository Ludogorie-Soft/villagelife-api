package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.LivingCondition;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import com.example.ludogorieSoft.village.repositories.VillageLivingConditionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VillageLivingConditionService {
    private final VillageLivingConditionRepository villageLivingConditionRepository;
    private final VillageService villageService;
    private final LivingConditionService livingConditionService;

    private final ModelMapper modelMapper;

    public VillageLivingConditionDTO toDTO(VillageLivingConditions forMap) {
        return modelMapper.map(forMap, VillageLivingConditionDTO.class);
    }

    public List<VillageLivingConditionDTO> getAllVillageLivingConditions() {
        List<VillageLivingConditions> villageLivingConditions = villageLivingConditionRepository.findAll();
        return villageLivingConditions.stream()
                .map(this::toDTO)
                .toList();
    }

    public VillageLivingConditionDTO getByID(Long id) {
        Optional<VillageLivingConditions> optionalVillageLivingConditions = villageLivingConditionRepository.findById(id);
        if (optionalVillageLivingConditions.isEmpty()) {
            throw new ApiRequestException("VillageLivingConditions Not Found");
        }
        return toDTO(optionalVillageLivingConditions.get());
    }


    public VillageLivingConditionDTO createVillageLivingCondition(VillageLivingConditionDTO villageLivingConditionsDTO) {
        VillageLivingConditions villageLivingConditions = new VillageLivingConditions();

        Village village = villageService.checkVillage(villageLivingConditionsDTO.getVillageId());
        villageLivingConditions.setVillage(village);

        LivingCondition livingCondition = livingConditionService.checkLivingCondition(villageLivingConditionsDTO.getLivingConditionId());
        villageLivingConditions.setLivingCondition(livingCondition);

        villageLivingConditions.setConsents(villageLivingConditionsDTO.getConsents());
        villageLivingConditionRepository.save(villageLivingConditions);
        return toDTO(villageLivingConditions);
    }

    public VillageLivingConditionDTO updateVillageLivingCondition(Long id, VillageLivingConditionDTO villageLivingConditionDTO) {
        Optional<VillageLivingConditions> villageLivingConditions = villageLivingConditionRepository.findById(id);
        if (villageLivingConditions.isEmpty()) {
            throw new ApiRequestException("Village living condition not found");
        }
        Village village = villageService.checkVillage(villageLivingConditionDTO.getVillageId());
        villageLivingConditions.get().setVillage(village);

        LivingCondition livingCondition = livingConditionService.checkLivingCondition(villageLivingConditionDTO.getLivingConditionId());
        villageLivingConditions.get().setLivingCondition(livingCondition);

        villageLivingConditions.get().setConsents(villageLivingConditionDTO.getConsents());
        villageLivingConditionRepository.save(villageLivingConditions.get());
        return toDTO(villageLivingConditions.get());
    }


    public int deleteVillageLivingConditions(Long id) {
        try {
            villageLivingConditionRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public List<VillageLivingConditionDTO> getVillageLivingConditionByVillageId(Long id) {
        List<VillageLivingConditions> villageLivingConditionsList = villageLivingConditionRepository.findAll();
        if (id != null) {
            villageLivingConditionsList = villageLivingConditionsList.stream()
                    .filter(assertion -> id.equals(assertion.getVillage().getId()))
                    .toList();
        }
        return villageLivingConditionsList.stream()
                .map(this::toDTO)
                .toList();
    }

    public double getVVillageLivingConditionByVillageIdValue(Long id) {
        List<VillageLivingConditions> villageLivingConditionsList = villageLivingConditionRepository.findAll();
        if (id != null) {
            villageLivingConditionsList = villageLivingConditionsList.stream()
                    .filter(assertion -> id.equals(assertion.getVillage().getId()))
                    .toList();
        }
        double sum = 0;
        int count = Math.min(8, villageLivingConditionsList.size());
        for (int i = 0; i < count; i++) {
            sum += villageLivingConditionsList.get(i).getConsents().getValue();
        }
        double average = sum / count;
        return Math.round(average * 100) / 100.0;
    }


    public double getVillageLivingConditionByVillageIdDelinquencyValue(Long id) {
        List<VillageLivingConditions> villageLivingConditionsList = villageLivingConditionRepository.findAll();
        if (id != null) {
            villageLivingConditionsList = villageLivingConditionsList.stream()
                    .filter(assertion -> id.equals(assertion.getVillage().getId()))
                    .toList();
        }
        for (VillageLivingConditions condition : villageLivingConditionsList) {
            if (condition.getLivingCondition().getLivingConditionName().equals("в селото няма престъпност")) {
                return Math.abs(condition.getConsents().getValue() - 100);
            }
        }
        return 50;
    }

    public double getVillageLivingConditionByVillageIdEcoValue(Long id) {
        List<VillageLivingConditions> villageLivingConditionsList = villageLivingConditionRepository.findAll();
        if (id != null) {
            villageLivingConditionsList = villageLivingConditionsList.stream()
                    .filter(assertion -> id.equals(assertion.getVillage().getId()))
                    .toList();
        }
        double sum = 0;
        int count = Math.min(13, villageLivingConditionsList.size());
        for (int i = 0; i < count; i++) {
            if (i >= 11) {
                sum += villageLivingConditionsList.get(i).getConsents().getValue();
            }
        }
        double average = sum / (count - 11);
        return Math.round(average * 100) / 100.0;
    }
}
