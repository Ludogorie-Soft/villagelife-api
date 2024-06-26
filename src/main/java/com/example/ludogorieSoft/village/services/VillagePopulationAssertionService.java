package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillagePopulationAssertionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillagePopulationAssertion;
import com.example.ludogorieSoft.village.repositories.VillagePopulationAssertionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class VillagePopulationAssertionService {
    private final VillagePopulationAssertionRepository villagePopulationAssertionRepository;
    private final PopulatedAssertionService populatedAssertionService;
    private final VillageService villageService;
    private final ModelMapper modelMapper;

    public VillagePopulationAssertionDTO toDTO(VillagePopulationAssertion villagePopulationAssertion) {
        return modelMapper.map(villagePopulationAssertion, VillagePopulationAssertionDTO.class);
    }

    public List<VillagePopulationAssertionDTO> getAllVillagePopulationAssertion() {
        List<VillagePopulationAssertion> villagePopulationAssertions = villagePopulationAssertionRepository.findAll();
        return villagePopulationAssertions
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public VillagePopulationAssertionDTO createVillagePopulationAssertionDTO(VillagePopulationAssertionDTO villagePopulationAssertionDTO) {
        VillagePopulationAssertion villagePopulationAssertion = new VillagePopulationAssertion();
        Village village = villageService.checkVillage(villagePopulationAssertionDTO.getVillageId());
        villagePopulationAssertion.setVillage(village);

        PopulatedAssertion populatedAssertion = populatedAssertionService.checkPopulatedAssertion(villagePopulationAssertionDTO.getPopulatedAssertionId());
        villagePopulationAssertion.setPopulatedAssertionID(populatedAssertion);

        villagePopulationAssertion.setAnswer(villagePopulationAssertionDTO.getAnswer());
        villagePopulationAssertion.setVillageStatus(villagePopulationAssertionDTO.getStatus());
        villagePopulationAssertion.setDateUpload(villagePopulationAssertionDTO.getDateUpload());
        villagePopulationAssertionRepository.save(villagePopulationAssertion);
        return toDTO(villagePopulationAssertion);
    }

    public VillagePopulationAssertionDTO getByID(Long id) {
        Optional<VillagePopulationAssertion> optionalVillagePopulationAssertion = villagePopulationAssertionRepository.findById(id);
        if (optionalVillagePopulationAssertion.isEmpty()) {
            throw new ApiRequestException("VillagePopulationAssertion Not Found ");
        }
        return toDTO(optionalVillagePopulationAssertion.get());
    }

    public int deleteVillagePopulationAssertion(Long id) {
        try {
            villagePopulationAssertionRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public VillagePopulationAssertionDTO updateVillagePopulationAssertion(Long id, VillagePopulationAssertionDTO villagePopulationAssertionDTO) {
        Optional<VillagePopulationAssertion> foundVillagePopulationAssertion = villagePopulationAssertionRepository.findById(id);
        if (foundVillagePopulationAssertion.isEmpty()) {
            throw new ApiRequestException("VillagePopulationAssertion not found");
        }
        Village village = villageService.checkVillage(villagePopulationAssertionDTO.getVillageId());
        foundVillagePopulationAssertion.get().setVillage(village);

        PopulatedAssertion populatedAssertion = populatedAssertionService.checkPopulatedAssertion(villagePopulationAssertionDTO.getPopulatedAssertionId());
        foundVillagePopulationAssertion.get().setPopulatedAssertionID(populatedAssertion);

        foundVillagePopulationAssertion.get().setAnswer(villagePopulationAssertionDTO.getAnswer());
        villagePopulationAssertionRepository.save(foundVillagePopulationAssertion.get());
        return toDTO(foundVillagePopulationAssertion.get());
    }

    public List<VillagePopulationAssertionDTO> getVillagePopulationAssertionByVillageId(Long id) {
        List<VillagePopulationAssertion> villagePopulationAssertionsList = villagePopulationAssertionRepository.findAll();

        if (id != null) {
            villagePopulationAssertionsList = villagePopulationAssertionsList.stream()
                    .filter(assertion -> id.equals(assertion.getVillage().getId()))
                    .toList();
        }

        return villagePopulationAssertionsList.stream()
                .map(this::toDTO)
                .toList();
    }

    public void updateVillagePopulationAssertionStatus(Long id, boolean status, String localDateTime) {
        List<VillagePopulationAssertion> villagePopulationAssertions = villagePopulationAssertionRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, localDateTime);

        List<VillagePopulationAssertion> villa = new ArrayList<>();

        if (!villagePopulationAssertions.isEmpty()) {
            for (VillagePopulationAssertion vill : villagePopulationAssertions) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setVillageStatus(true);
                vill.setDateDeleted(null);
                villa.add(vill);
            }
            villagePopulationAssertionRepository.saveAll(villa);
        }
    }
    public void rejectVillagePopulationAssertionStatus(Long id, boolean status, String responseDate, LocalDateTime deleteDate) {
        List<VillagePopulationAssertion> villagePopulationAssertions = villagePopulationAssertionRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, responseDate);

        List<VillagePopulationAssertion> villa = new ArrayList<>();

        if (!villagePopulationAssertions.isEmpty()) {
            for (VillagePopulationAssertion vill : villagePopulationAssertions) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setDateDeleted(deleteDate);
                villa.add(vill);
            }
            villagePopulationAssertionRepository.saveAll(villa);
        }
    }
}
