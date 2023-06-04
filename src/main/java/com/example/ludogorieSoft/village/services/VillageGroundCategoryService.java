package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.model.GroundCategory;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import com.example.ludogorieSoft.village.repositories.GroundCategoryRepository;
import com.example.ludogorieSoft.village.repositories.VillageGroundCategoryRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class VillageGroundCategoryService {

    private final VillageGroundCategoryRepository villageGroundCategoryRepository;
    private final VillageRepository villageRepository;
    private final GroundCategoryRepository groundCategoryRepository;
    private final ModelMapper modelMapper;
    private final  String errorMessage="Village not found";

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
        Optional<Village> village = villageRepository.findById(villageGroundCategoryDTO.getVillageId());
        if (village.isPresent()){
            villageGroundCategory.setVillage(village.get());
        }else {
            throw new ApiRequestException(errorMessage);
        }
        Optional<GroundCategory> groundCategory = groundCategoryRepository.findById(villageGroundCategoryDTO.getGroundCategoryId());
        if (groundCategory.isPresent()){
            villageGroundCategory.setGroundCategory(groundCategory.get());
        }else {
            throw new ApiRequestException("GroundCategory not found");
        }
        villageGroundCategoryRepository.save(villageGroundCategory);
        return toDTO(villageGroundCategory);
    }


    public VillageGroundCategoryDTO updateVillageGroundCategory(Long id, VillageGroundCategoryDTO villageGroundCategoryDTO) {
        Optional<VillageGroundCategory> foundVillageGroundCategory = villageGroundCategoryRepository.findById(id);
        if (foundVillageGroundCategory.isEmpty()) {
            throw new ApiRequestException("Village Ground Category Not Found");
        }
        Optional<Village> village = villageRepository.findById(villageGroundCategoryDTO.getVillageId());
        if (village.isPresent()){
            foundVillageGroundCategory.get().setVillage(village.get());
        }else {
            throw new ApiRequestException(errorMessage);
        }
        Optional<GroundCategory> groundCategory = groundCategoryRepository.findById(villageGroundCategoryDTO.getGroundCategoryId());
        if (groundCategory.isPresent()){
            foundVillageGroundCategory.get().setGroundCategory(groundCategory.get());
        }else {
            throw new ApiRequestException(errorMessage);
        }
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

}
