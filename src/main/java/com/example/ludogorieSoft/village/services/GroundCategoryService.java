package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.GroundCategoryDTO;
import com.example.ludogorieSoft.village.model.GroundCategory;
import com.example.ludogorieSoft.village.repositories.GroundCategoryRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroundCategoryService {

    private final GroundCategoryRepository groundCategoryRepository;
    private final ModelMapper modelMapper;

    public GroundCategoryDTO toDTO(GroundCategory forMap) {
        return modelMapper.map(forMap, GroundCategoryDTO.class);
    }


    public List<GroundCategoryDTO> getAllGroundCategories() {
        List<GroundCategory> groundCategories = groundCategoryRepository.findAll();
        return groundCategories.stream()
                .map(this::toDTO)
                .toList();
    }

    public GroundCategoryDTO getByID(Long id) {
        Optional<GroundCategory> optionalGroundCategory = groundCategoryRepository.findById(id);
        if (optionalGroundCategory.isEmpty()) {
            throw new ApiRequestException("Ground Category with id: " + id + " Not Found");
        }
        return toDTO(optionalGroundCategory.get());
    }



    public GroundCategoryDTO createGroundCategoryDTO(GroundCategoryDTO groundCategoryDTO) {
        if (groundCategoryRepository.existsByGroundCategoryName(groundCategoryDTO.getGroundCategoryName())) {
            throw new ApiRequestException("Ground Category with name: " + groundCategoryDTO.getGroundCategoryName() + " already exists");
        }

        GroundCategory groundCategory = new GroundCategory();
        groundCategory.setGroundCategoryName(groundCategoryDTO.getGroundCategoryName());
        groundCategoryRepository.save(groundCategory);

        return toDTO(groundCategory);
    }


    public GroundCategoryDTO updateGroundCategory(Long id, GroundCategoryDTO groundCategoryDTO) {
        Optional<GroundCategory> foundGroundCategory = groundCategoryRepository.findById(id);
        if (foundGroundCategory.isEmpty()) {
            throw new ApiRequestException("Ground Category Not Found");
        }

        GroundCategory groundCategory = foundGroundCategory.get();
        groundCategory.setGroundCategoryName(groundCategoryDTO.getGroundCategoryName());

        groundCategoryRepository.save(groundCategory);
        return toDTO(groundCategory);
    }


    public void deleteGroundCategory(Long id) {
        Optional<GroundCategory> groundCategory = groundCategoryRepository.findById(id);

        if (groundCategory.isEmpty()) {
            throw new ApiRequestException("Ground Category not found for id " + id);
        }
        groundCategoryRepository.delete(groundCategory.get());
    }

    public GroundCategory checkGroundCategory(Long id) {
        Optional<GroundCategory> groundCategory = groundCategoryRepository.findById(id);
        if (groundCategory.isPresent()){
            return groundCategory.get();
        }else {
            throw new ApiRequestException("Ground Category not found");
        }
    }

}
