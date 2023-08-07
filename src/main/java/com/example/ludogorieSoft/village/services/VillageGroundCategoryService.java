package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.VillageGroundCategoryRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
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
public class VillageGroundCategoryService {

    private final VillageGroundCategoryRepository villageGroundCategoryRepository;
    private final ModelMapper modelMapper;
    private final VillageService villageService;
    private final GroundCategoryService groundCategoryService;

    public VillageGroundCategoryDTO toDTO(VillageGroundCategory forMap) {
        return modelMapper.map(forMap, VillageGroundCategoryDTO.class);
    }

    public List<VillageGroundCategoryDTO> getAllVillageGroundCategories() {
        List<VillageGroundCategory> villageGroundCategories = villageGroundCategoryRepository.findAll();
        return villageGroundCategories.stream()
                .map(this::toDTO)
                .toList();
    }

    public VillageGroundCategoryDTO getByID(Long id) {
        Optional<VillageGroundCategory> optionalVillageGroundCategory = villageGroundCategoryRepository.findById(id);
        if (optionalVillageGroundCategory.isEmpty()) {
            throw new ApiRequestException("Village Ground Category with id: " + id + " Not Found");
        }
        return toDTO(optionalVillageGroundCategory.get());
    }

    public VillageGroundCategoryDTO createVillageGroundCategoryDTO(VillageGroundCategoryDTO villageGroundCategoryDTO) {
        VillageGroundCategory villageGroundCategory = new VillageGroundCategory();

        Village village = villageService.checkVillage(villageGroundCategoryDTO.getVillageId());
        villageGroundCategory.setVillage(village);

        GroundCategory groundCategory = groundCategoryService.checkGroundCategory(villageGroundCategoryDTO.getGroundCategoryId());
        villageGroundCategory.setGroundCategory(groundCategory);

        villageGroundCategory.setVillageStatus(villageGroundCategoryDTO.getStatus());
        villageGroundCategory.setDateUpload(villageGroundCategoryDTO.getDateUpload());

        villageGroundCategoryRepository.save(villageGroundCategory);
        return toDTO(villageGroundCategory);
    }

    public VillageGroundCategoryDTO updateVillageGroundCategory(Long id, VillageGroundCategoryDTO villageGroundCategoryDTO) {
        Optional<VillageGroundCategory> foundVillageGroundCategory = villageGroundCategoryRepository.findById(id);
        if (foundVillageGroundCategory.isEmpty()) {
            throw new ApiRequestException("Village Ground Category Not Found");
        }

        Village village = villageService.checkVillage(villageGroundCategoryDTO.getVillageId());
        foundVillageGroundCategory.get().setVillage(village);

        GroundCategory groundCategory = groundCategoryService.checkGroundCategory(villageGroundCategoryDTO.getGroundCategoryId());
        foundVillageGroundCategory.get().setGroundCategory(groundCategory);

        villageGroundCategoryRepository.save(foundVillageGroundCategory.get());
        return toDTO(foundVillageGroundCategory.get());
    }

    public int deleteVillageGroundCategory(Long id) {
        try {
            villageGroundCategoryRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public VillageGroundCategoryDTO findVillageGroundCategoryDTOByVillageId(Long villageId){
        VillageGroundCategory villageGroundCategory = villageGroundCategoryRepository.findByVillageId(villageId);
        if(villageGroundCategory == null){
            throw new ApiRequestException("Village ground category with village id " + villageId + " not found");
        }
        return toDTO(villageGroundCategory);
    }

    public void updateVillageGroundCategoryStatus(Long id, boolean status, String localDateTime) {
        List<VillageGroundCategory> villagelivingconditions = villageGroundCategoryRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, localDateTime);

        List<VillageGroundCategory> villa = new ArrayList<>();

        if (!villagelivingconditions.isEmpty()) {
            for (VillageGroundCategory vill : villagelivingconditions) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setVillageStatus(true);
                villa.add(vill);
            }
            villageGroundCategoryRepository.saveAll(villa);
        }
    }
    public void rejectVillageGroundCategoryResponse(Long id, boolean status, String responseDate, LocalDateTime dateDelete) {
        List<VillageGroundCategory> villagelivingconditions = villageGroundCategoryRepository.findByVillageIdAndVillageStatusAndDateUpload(
                id, status, responseDate
        );
        List<VillageGroundCategory> villa = new ArrayList<>();

        if (!villagelivingconditions.isEmpty()) {
            for (VillageGroundCategory vill : villagelivingconditions) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setDateDeleted(dateDelete);
                villa.add(vill);
            }
            villageGroundCategoryRepository.saveAll(villa);
        }
    }

}
