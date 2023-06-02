package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.model.ObjectVillage;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.ObjectAroundVillageRepository;
import com.example.ludogorieSoft.village.repositories.ObjectVillageRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ObjectVillageService {
    private final ModelMapper modelMapper;
    private final ObjectAroundVillageRepository objectAroundVillageRepository;
    private final VillageRepository villageRepository;
    private final ObjectVillageRepository objectVillageRepository;

    public ObjectVillageDTO objectVillageToObjectVillageDTO(ObjectVillage objectVillage) {
        return modelMapper.map(objectVillage, ObjectVillageDTO.class);
    }

    public List<ObjectVillageDTO> getAllObjectVillages() {
        List<ObjectVillage> objectVillages = objectVillageRepository.findAll();
        return objectVillages
                .stream()
                .map(this::objectVillageToObjectVillageDTO)
                .collect(Collectors.toList());
    }

    public ObjectVillageDTO getObjectVillageById(Long id) {
        Optional<ObjectVillage> objectVillage = objectVillageRepository.findById(id);
        if (objectVillage.isEmpty()) {
            throw new ApiRequestException("ObjectVillage not found");
        }
        return objectVillageToObjectVillageDTO(objectVillage.get());
    }

    public ObjectVillageDTO updateObjectVillageById(Long id, ObjectVillageDTO objectVillageDTO) {
        Optional<ObjectVillage> foundObjectVillage = objectVillageRepository.findById(id);
        if (foundObjectVillage.isEmpty()) {
            throw new ApiRequestException("ObjectVillage not found");
        }
        Optional<ObjectAroundVillage> objectAroundVillage = objectAroundVillageRepository.findById(objectVillageDTO.getObjectAroundVillageId());
        if (objectAroundVillage.isPresent()) {
            foundObjectVillage.get().setObject(objectAroundVillage.get());
        } else {
            throw new ApiRequestException("Object not found");
        }
        Optional<Village> village = villageRepository.findById(objectVillageDTO.getVillageId());
        if (village.isPresent()) {
            foundObjectVillage.get().setVillage(village.get());
        } else {
            throw new ApiRequestException("Village not found");
        }
        foundObjectVillage.get().setDistance(objectVillageDTO.getDistance());
        objectVillageRepository.save(foundObjectVillage.get());
        return objectVillageDTO;
    }

    public ObjectVillageDTO createObjectVillage(ObjectVillageDTO objectVillageDTO) {
        ObjectVillage objectVillage = new ObjectVillage();
        Optional<Village> village = villageRepository.findById(objectVillageDTO.getVillageId());
        if (village.isPresent()) {
            objectVillage.setVillage(village.get());
        } else {
            throw new ApiRequestException("Village not found");
        }
        Optional<ObjectAroundVillage> objectAroundVillage = objectAroundVillageRepository.findById(objectVillageDTO.getObjectAroundVillageId());
        if (objectAroundVillage.isPresent()) {
            objectVillage.setObject(objectAroundVillage.get());
        } else {
            throw new ApiRequestException("Object not found");
        }
        objectVillage.setDistance(objectVillageDTO.getDistance());
        objectVillageRepository.save(objectVillage);
        return objectVillageDTO;
    }


    public void deleteObjectVillageById(Long id) {
        Optional<ObjectVillage> objectVillage = objectVillageRepository.findById(id);
        if (objectVillage.isEmpty()) {
            throw new ApiRequestException("ObjectVillage not found for id " + id);
        }
        objectVillageRepository.delete(objectVillage.get());
    }
}
