package com.example.ludogorieSoft.village.Services;

import com.example.ludogorieSoft.village.DTOs.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.Model.VillageGroundCategory;
import com.example.ludogorieSoft.village.Repositories.VillageGroundCategoryRepository;
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

    public VillageGroundCategoryDTO toDTO(VillageGroundCategory forMap){
        return modelMapper.map(forMap, VillageGroundCategoryDTO.class);
    }


    public List<VillageGroundCategoryDTO> getAllVillageGroundCategories(){
        List<VillageGroundCategory> villageGroundCategories = villageGroundCategoryRepository.findAll();
        return villageGroundCategories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VillageGroundCategoryDTO createVillageGroundCategoryDTO(VillageGroundCategory villageGroundCategory) {
        villageGroundCategoryRepository.save(villageGroundCategory);
        return toDTO(villageGroundCategory);
    }

    public VillageGroundCategoryDTO getByID(Long id){
        Optional<VillageGroundCategory> optionalVillageGroundCategory = villageGroundCategoryRepository.findById(id);
        if (optionalVillageGroundCategory.isEmpty()){
            throw new ApiRequestException("VillageGroundCategory Not Found");
        }
        return toDTO(optionalVillageGroundCategory.get());
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
