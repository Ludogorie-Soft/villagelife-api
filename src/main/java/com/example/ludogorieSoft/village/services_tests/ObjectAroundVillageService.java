package com.example.ludogorieSoft.village.services_tests;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.repositories.ObjectAroundVillageRepository;
import lombok.AllArgsConstructor;
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
        List<ObjectAroundVillage> objectAroundVillages = objectAroundVillageRepository.findAll();
        return objectAroundVillages.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ObjectAroundVillageDTO getObjectAroundVillageById(Long id) {
        Optional<ObjectAroundVillage> question = objectAroundVillageRepository.findById(id);
        if (question.isEmpty()) {
            throw new ApiRequestException("Object Around Village with id: " + id + " Not Found");
        }
        return convertToDTO(question.get());
    }


    public ObjectAroundVillageDTO createObjectAroundVillage(ObjectAroundVillageDTO objectAroundVillageDTO) {
        if (objectAroundVillageRepository.existsByType(objectAroundVillageDTO.getType())) {
            throw new ApiRequestException("Object Around Village with name: " + objectAroundVillageDTO.getType() + " already exists");
        }
        ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage();
        objectAroundVillage.setType(objectAroundVillageDTO.getType());
        objectAroundVillageRepository.save(objectAroundVillage);

        return objectAroundVillageDTO;
    }


    public ObjectAroundVillageDTO updateObjectAroundVillage(Long id, ObjectAroundVillage objectAroundVillage) {
        Optional<ObjectAroundVillage> findObjectAroundVillage = objectAroundVillageRepository.findById(id);
        if (findObjectAroundVillage.isEmpty()) {
            throw new ApiRequestException("Object Around Village with id: " + id + " Not Found");
        }
        if (objectAroundVillageRepository.existsByType(objectAroundVillage.getType())) {
            throw new ApiRequestException("Object Around Village with type: " + objectAroundVillage.getType() + " already exists");
        }
        findObjectAroundVillage.get().setType(objectAroundVillage.getType());
        objectAroundVillageRepository.save(findObjectAroundVillage.get());
        return convertToDTO(findObjectAroundVillage.get());
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
