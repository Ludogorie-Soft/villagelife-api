package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.GroundCategoryDTO;
import com.example.ludogoriesoft.village.model.GroundCategory;
import com.example.ludogoriesoft.village.repositories.GroundCategoryRepository;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
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
        if (StringUtils.isBlank(groundCategoryDTO.getGroundCategoryName())) {
            throw new ApiRequestException("Ground Category is blank");
        }
        if (groundCategoryRepository.existsByGroundCategoryName(groundCategoryDTO.getGroundCategoryName())) {
            throw new ApiRequestException("Ground Category with name: " + groundCategoryDTO.getGroundCategoryName() + " already exists");
        }
        GroundCategory groundCategory = new GroundCategory();
        groundCategory.setGroundCategoryName(groundCategoryDTO.getGroundCategoryName());
        groundCategoryRepository.save(groundCategory);
        return groundCategoryDTO;
    }


    public GroundCategoryDTO updateGroundCategory(Long id, GroundCategoryDTO groundCategoryDTO) {
        Optional<GroundCategory> foundGroundCategory = groundCategoryRepository.findById(id);
        GroundCategory livingCondition = foundGroundCategory.orElseThrow(() -> new ApiRequestException("Living condition not found"));

        if (groundCategoryDTO == null || groundCategoryDTO.getGroundCategoryName() == null || groundCategoryDTO.getGroundCategoryName().isEmpty()) {
            throw new ApiRequestException("Invalid Living Condition data");
        }

        String newGroundCategoryName = groundCategoryDTO.getGroundCategoryName();
        if (!newGroundCategoryName.equals(livingCondition.getGroundCategoryName())) {
            if (groundCategoryRepository.existsByGroundCategoryName(newGroundCategoryName)) {
                throw new ApiRequestException("Living Condition with name: " + newGroundCategoryName + " already exists");
            }
            livingCondition.setGroundCategoryName(newGroundCategoryName);
            groundCategoryRepository.save(livingCondition);
        }

        return toDTO(livingCondition);
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
