package com.example.ludogorieSoft.village.services_tests;

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

@Service
@AllArgsConstructor
public class ObjectVillageService {
    private final ModelMapper modelMapper;
    private final ObjectAroundVillageRepository objectAroundVillageRepository;
    private final VillageRepository villageRepository;
    private final ObjectVillageRepository objectVillageRepository;
    private final VillageService villageService;
    private final ObjectAroundVillageService objectAroundVillageService;

    public ObjectVillageDTO objectVillageToObjectVillageDTO(ObjectVillage objectVillage) {
        return modelMapper.map(objectVillage, ObjectVillageDTO.class);
    }

    public List<ObjectVillageDTO> getAllObjectVillages() {
        List<ObjectVillage> objectVillages = objectVillageRepository.findAll();
        return objectVillages
                .stream()
                .map(this::objectVillageToObjectVillageDTO)
                .toList();
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
        Village village = villageService.checkVillage(objectVillageDTO.getVillageId());
        foundObjectVillage.get().setVillage(village);

        ObjectAroundVillage objectAroundVillage = objectAroundVillageService.checkObject(objectVillageDTO.getObjectAroundVillageId());
        foundObjectVillage.get().setObject(objectAroundVillage);

        foundObjectVillage.get().setDistance(objectVillageDTO.getDistance());
        objectVillageRepository.save(foundObjectVillage.get());
        return objectVillageDTO;
    }

    public ObjectVillageDTO createObjectVillage(ObjectVillageDTO objectVillageDTO) {
        ObjectVillage objectVillage = new ObjectVillage();

        Village village = villageService.checkVillage(objectVillageDTO.getVillageId());
        objectVillage.setVillage(village);

        ObjectAroundVillage objectAroundVillage = objectAroundVillageService.checkObject(objectVillageDTO.getObjectAroundVillageId());
        objectVillage.setObject(objectAroundVillage);

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
