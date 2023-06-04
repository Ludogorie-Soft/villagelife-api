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
    private final VillageService villageService;
    private final GroundCategoryService groundCategoryService;
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

        Village village = villageService.checkVillage(villageGroundCategoryDTO.getVillageId());
        villageGroundCategory.setVillage(village);

        GroundCategory groundCategory = groundCategoryService.checkGroundCategory(villageGroundCategoryDTO.getGroundCategoryId());
        villageGroundCategory.setGroundCategory(groundCategory);

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

}
