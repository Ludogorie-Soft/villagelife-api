package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import com.example.ludogorieSoft.village.repositories.VillageGroundCategoryRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VillageGroundCategoryService {

    private final VillageGroundCategoryRepository villageGroundCategoryRepository;
    private final ModelMapper modelMapper;

    public VillageGroundCategoryDTO toDTO(VillageGroundCategory forMap) {
        return modelMapper.map(forMap, VillageGroundCategoryDTO.class);
    }


    public List<VillageGroundCategoryDTO> getAllVillageGroundCategories() {
        List<VillageGroundCategory> villageGroundCategories = villageGroundCategoryRepository.findAll();
        return villageGroundCategories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VillageGroundCategoryDTO getByID(Long id) {
        Optional<VillageGroundCategory> optionalVillageGroundCategory = villageGroundCategoryRepository.findById(id);
        if (optionalVillageGroundCategory.isEmpty()) {
            throw new ApiRequestException("Village Ground Category with id: " + id + " Not Found");
        }
        return toDTO(optionalVillageGroundCategory.get());
    }

    public VillageGroundCategoryDTO createVillageGroundCategoryDTO(VillageGroundCategory villageGroundCategory) {
        villageGroundCategoryRepository.save(villageGroundCategory);
        return toDTO(villageGroundCategory);
    }

    public VillageGroundCategoryDTO updateVillageGroundCategory(Long id, VillageGroundCategory villageGroundCategory) {
        Optional<VillageGroundCategory> foundVillageGroundCategory = villageGroundCategoryRepository.findById(id);
        if (foundVillageGroundCategory.isEmpty()) {
            throw new ApiRequestException("Village Ground Category Not Found");
        }
        foundVillageGroundCategory.get().setId(villageGroundCategory.getId());
        foundVillageGroundCategory.get().setVillage(villageGroundCategory.getVillage());
        foundVillageGroundCategory.get().setGroundCategory(villageGroundCategory.getGroundCategory());

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
