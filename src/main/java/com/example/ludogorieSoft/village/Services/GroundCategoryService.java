package com.example.ludogorieSoft.village.Services;

import com.example.ludogorieSoft.village.DTOs.GroundCategoryDTO;
import com.example.ludogorieSoft.village.Model.GroundCategory;
import com.example.ludogorieSoft.village.Repositories.GroundCategoryRepository;
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
public class GroundCategoryService {

    private final GroundCategoryRepository groundCategoryRepository;
    private final ModelMapper modelMapper;

    public GroundCategoryDTO toDTO(GroundCategory forMap){
        return modelMapper.map(forMap, GroundCategoryDTO.class);
    }


    public List<GroundCategoryDTO> getAllGroundCategories(){
        List<GroundCategory> groundCategories = groundCategoryRepository.findAll();
        return groundCategories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public GroundCategoryDTO createGroundCategoryDTO(GroundCategory groundCategory) {
        groundCategoryRepository.save(groundCategory);
        return toDTO(groundCategory);
    }

    public GroundCategoryDTO getByID(Long id){
        Optional<GroundCategory> optionalGroundCategory = groundCategoryRepository.findById(id);
        if (optionalGroundCategory.isEmpty()){
            throw new ApiRequestException("Ground Category Not Found");
        }
        return toDTO(optionalGroundCategory.get());
    }

    public int deleteGroundCategory(Long id) {
        try {
            groundCategoryRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
}
