package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.repositories.ObjectAroundVillageRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ObjectAroundVillageService {

    private ObjectAroundVillageRepository objectAroundVillageRepository;
    private final ModelMapper modelMapper;

    public ObjectAroundVillageDTO convertToDTO(ObjectAroundVillage objectAroundVillage) {
        return modelMapper.map(objectAroundVillage, ObjectAroundVillageDTO.class);
    }

    public List<ObjectAroundVillageDTO> getAllObjectsAroundVillage() {
        List<ObjectAroundVillage> objectAroundVillages = objectAroundVillageRepository.findAllByOrderByIdAsc();
        return objectAroundVillages.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ObjectAroundVillageDTO getObjectAroundVillageById(Long id) {
        Optional<ObjectAroundVillage> objectAroundVillage = objectAroundVillageRepository.findById(id);
        if (objectAroundVillage.isEmpty()) {
            throw new ApiRequestException("Object Around Village with id: " + id + " Not Found");
        }
        return convertToDTO(objectAroundVillage.get());
    }


    public ObjectAroundVillageDTO createObjectAroundVillage(ObjectAroundVillageDTO objectAroundVillageDTO) {
        if (StringUtils.isBlank(objectAroundVillageDTO.getType())) {
            throw new ApiRequestException("Object Around Village is blank");
        }
        if (objectAroundVillageRepository.existsByType(objectAroundVillageDTO.getType())) {
            throw new ApiRequestException("Object Around Village with name: " + objectAroundVillageDTO.getType() + " already exists");
        }
        ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage();
        objectAroundVillage.setType(objectAroundVillageDTO.getType());
        objectAroundVillageRepository.save(objectAroundVillage);
        return objectAroundVillageDTO;
    }


    public ObjectAroundVillageDTO updateObjectAroundVillage(Long id, ObjectAroundVillageDTO objectAroundVillageDTO) {
        Optional<ObjectAroundVillage> foundObjectAroundVillage = objectAroundVillageRepository.findById(id);
        ObjectAroundVillage objectAroundVillage = foundObjectAroundVillage.orElseThrow(() -> new ApiRequestException("Landscape not found"));

        if (objectAroundVillageDTO == null || objectAroundVillageDTO.getType() == null || objectAroundVillageDTO.getType().isEmpty()) {
            throw new ApiRequestException("Invalid Landscape data");
        }

        String newObjectAroundVillageType = objectAroundVillageDTO.getType();
        if (!newObjectAroundVillageType.equals(objectAroundVillage.getType())) {
            if (objectAroundVillageRepository.existsByType(newObjectAroundVillageType)) {
                throw new ApiRequestException("Landscape with name: " + newObjectAroundVillageType + " already exists");
            }
            objectAroundVillage.setType(newObjectAroundVillageType);
            objectAroundVillageRepository.save(objectAroundVillage);
        }

        return convertToDTO(objectAroundVillage);
    }


    public void deleteObjectAroundVillageById(Long id) {
        Optional<ObjectAroundVillage> objectAroundVillage = objectAroundVillageRepository.findById(id);
        if (objectAroundVillage.isEmpty()) {
            throw new ApiRequestException("Object Around Village not found for id " + id);
        }
        objectAroundVillageRepository.delete(objectAroundVillage.get());
    }

    public ObjectAroundVillage checkObject(Long id) {
        Optional<ObjectAroundVillage> objectAroundVillage = objectAroundVillageRepository.findById(id);
        if (objectAroundVillage.isPresent()){
            return objectAroundVillage.get();
        }else {
            throw new ApiRequestException("Object Around Village not found");
        }
    }
}
