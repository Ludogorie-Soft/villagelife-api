package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.LivingCondition;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import com.example.ludogorieSoft.village.repositories.VillageLivingConditionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public VillageLivingConditionDTO createVillageLivingCondition(VillageLivingConditionDTO villageLivingConditionsDTO) {
        VillageLivingConditions villageLivingConditions = new VillageLivingConditions();

        Village village = villageService.checkVillage(villageLivingConditionsDTO.getVillageId());
        villageLivingConditions.setVillage(village);

        LivingCondition livingCondition = livingConditionService.checkLivingCondition(villageLivingConditionsDTO.getLivingConditionId());
        villageLivingConditions.setLivingCondition(livingCondition);

        villageLivingConditions.setConsents(villageLivingConditionsDTO.getConsents());
        villageLivingConditions.setVillageStatus(villageLivingConditionsDTO.getStatus());
        villageLivingConditions.setDateUpload(villageLivingConditionsDTO.getDateUpload());
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

    public void updateVillageLivingConditionStatus(Long id, boolean status, String localDateTime) {
        List<VillageLivingConditions> villageLivingConditions = villageLivingConditionRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, localDateTime);

        List<VillageLivingConditions> villa = new ArrayList<>();

        if (!villageLivingConditions.isEmpty()) {
            for (VillageLivingConditions vill : villageLivingConditions) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setVillageStatus(true);
                vill.setDateDeleted(null);
                villa.add(vill);
            }
            villageLivingConditionRepository.saveAll(villa);
        }
    }
    public void rejectVillageLivingConditionResponse(Long id, boolean status, String responseDate, LocalDateTime dateDelete) {
        List<VillageLivingConditions> villageLivingConditions = villageLivingConditionRepository.findByVillageIdAndVillageStatusAndDateUpload(
                id, status, responseDate
        );
        List<VillageLivingConditions> villa = new ArrayList<>();

        if (!villageLivingConditions.isEmpty()) {
            for (VillageLivingConditions vill : villageLivingConditions) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setDateDeleted(dateDelete);
                villa.add(vill);
            }
            villageLivingConditionRepository.saveAll(villa);
        }
    }
}
