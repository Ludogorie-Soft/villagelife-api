package com.example.ludogorieSoft.village.Services;

import com.example.ludogorieSoft.village.DTOs.GroundCategoryDTO;
import com.example.ludogorieSoft.village.Model.GroundCategory;
import com.example.ludogorieSoft.village.Repositories.GroundCategoryRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    public GroundCategoryDTO getByID(Long id) {
        Optional<GroundCategory> optionalGroundCategory = groundCategoryRepository.findById(id);
        if (optionalGroundCategory.isEmpty()) {
            throw new ApiRequestException("Ground Category with id: " + id + " Not Found");
        }
        return toDTO(optionalGroundCategory.get());
    }


//    public GroundCategoryDTO createGroundCategoryDTO(GroundCategoryDTO groundCategoryDTO) {
//        GroundCategory existingGroundCategory = groundCategoryRepository.findByGroundCategoryName(groundCategoryDTO.getGroundCategory());
//
//        if (existingGroundCategory != null) {
//            System.out.println("ВЛИЗАМЕ В ----------------> IF");
//            throw new ApiRequestException("Ground Category with name: " + groundCategoryDTO.getGroundCategory() + " already exists");
//        }
//
//        GroundCategory groundCategory = new GroundCategory();
//        groundCategory.setGroundCategory(groundCategoryDTO.getGroundCategory());
//        groundCategoryRepository.save(groundCategory);
//
//        return groundCategoryDTO;
////        GroundCategory groundCategory = modelMapper.map(groundCategoryDTO, GroundCategory.class);
////
////        return modelMapper.map(groundCategoryRepository.save(groundCategory), GroundCategoryDTO.class);
//    }

    public GroundCategoryDTO createGroundCategoryDTO(GroundCategoryDTO groundCategoryDTO) {
        if (groundCategoryRepository.existsByGroundCategory(groundCategoryDTO.getGroundCategory())) {
            throw new ApiRequestException("Ground Category with name: " + groundCategoryDTO.getGroundCategory() + " already exists");
        }

        GroundCategory groundCategory = new GroundCategory();
        groundCategory.setGroundCategory(groundCategoryDTO.getGroundCategory());
        groundCategoryRepository.save(groundCategory);

        return groundCategoryDTO;
    }


    public GroundCategoryDTO updateGroundCategory(Long id, GroundCategory groundCategory) {
        Optional<GroundCategory> foundGroundCategory = groundCategoryRepository.findById(id);
        if (foundGroundCategory.isEmpty()) {
            throw new ApiRequestException("Ground Category Not Found");
        }
        foundGroundCategory.get().setGroundCategory(groundCategory.getGroundCategory());

        groundCategoryRepository.save(foundGroundCategory.get());
        return toDTO(foundGroundCategory.get());
    }


    public void deleteGroundCategory(Long id) {
        Optional<GroundCategory> groundCategory = groundCategoryRepository.findById(id);

        if (groundCategory.isEmpty()) {
            throw new ApiRequestException("Ground Category not found for id " + id);
        }
        groundCategoryRepository.delete(groundCategory.get());
    }
}
