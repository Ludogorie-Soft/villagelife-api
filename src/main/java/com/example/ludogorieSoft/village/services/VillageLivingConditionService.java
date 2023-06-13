package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogoriesoft.village.dtos.VillagePopulationAssertionDTO;
import com.example.ludogoriesoft.village.model.*;
import com.example.ludogoriesoft.village.repositories.LivingConditionRepository;
import com.example.ludogoriesoft.village.repositories.VillageLivingConditionRepository;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import com.example.ludogoriesoft.village.repositories.VillageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VillageLivingConditionService {
    private final VillageLivingConditionRepository villageLivingConditionRepository;

    private final LivingConditionRepository livingConditionRepository;
    private final VillageRepository villageRepository;
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

    public List<VillageLivingConditionDTO> getVillagePopulationAssertionByVillageId(Long id) {
        List<VillageLivingConditions> villageLivingConditionsList = villageLivingConditionRepository.findAll();
        if (id != null) {
            villageLivingConditionsList = villageLivingConditionsList.stream()
                    .filter(assertion -> id.equals(assertion.getVillage().getId()))
                    .collect(Collectors.toList());
        }
        return villageLivingConditionsList.stream()
                .map(this::toDTO)
                .toList();
    }


}
