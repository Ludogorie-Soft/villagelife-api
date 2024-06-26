package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.dtos.response.ObjectVillageResponse;
import com.example.ludogorieSoft.village.enums.Distance;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.Set;

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
        objectVillage.setVillageStatus(objectVillageDTO.getStatus());
        objectVillage.setDateUpload(objectVillageDTO.getDateUpload());
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


    public List<ObjectVillageDTO> getObjectVillageByVillageId(Long id) {
        List<ObjectVillage> objectVillageList = objectVillageRepository.findAll();

        List<ObjectVillage> filteredList = objectVillageList.stream()
                .filter(obj -> obj.getVillage() != null && obj.getVillage().getId().equals(id))
                .toList();

        return filteredList.stream()
                .map(this::objectVillageToObjectVillageDTO)
                .toList();
    }

    public boolean existsByVillageIdAndObjectIdAndDistance(Long villageId, Long objectId, Distance distance) {
        return objectVillageRepository.existsByVillageIdAndObjectIdAndDistance(villageId, objectId, distance);
    }

    public List<ObjectVillageDTO> getDistinctObjectVillagesByVillageId(Long villageId, boolean status, String date) {
        List<ObjectVillage> allObjectVillages;
        if (status) {
            allObjectVillages = objectVillageRepository.findByVillageIdAndVillageStatus(villageId, true);
        } else {
            allObjectVillages = objectVillageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, status, date);
        }
        Set<String> uniqueCombinations = new HashSet<>();
        List<ObjectVillageDTO> filteredObjectVillages = new ArrayList<>();

        for (ObjectVillage objectVillage : allObjectVillages) {
            String key = objectVillage.getObject().getId() + "-" + objectVillage.getDistance();
            if (uniqueCombinations.add(key)) {
                filteredObjectVillages.add(objectVillageToObjectVillageDTO(objectVillage));
            }
        }

        return filteredObjectVillages;
    }

    public List<ObjectVillageResponse> getObjectVillageResponses(List<ObjectVillageDTO> objectVillageDTOS) {
        List<ObjectVillageResponse> objectVillageResponses = new ArrayList<>();
        Distance[] distances = Distance.values();
        for (Distance distance : distances) {
            List<ObjectVillageDTO> filteredList = objectVillageDTOS.stream()
                    .filter(obj -> obj.getDistance() == distance)
                    .toList();
            if (distance != Distance.OVER_50_KM && !filteredList.isEmpty()) {
                ObjectVillageResponse objectVillageResponse = new ObjectVillageResponse();
                objectVillageResponse.setDistance(distance);
                List<ObjectAroundVillageDTO> objects = filteredList
                        .stream()
                        .map(obj -> objectAroundVillageService.getObjectAroundVillageById(obj.getObjectAroundVillageId()))
                        .toList();

                objectVillageResponse.setObjects(objects);
                objectVillageResponses.add(objectVillageResponse);
            }
        }
        return objectVillageResponses;
    }

    public List<ObjectVillageDTO> getDistinctObjectVillagesByVillageIdForAdmin(Long villageId, boolean status) {
        List<ObjectVillage> allObjectVillages = objectVillageRepository.findByVillageIdAndVillageStatus(villageId, status);
        Set<String> uniqueCombinations = new HashSet<>();
        List<ObjectVillageDTO> filteredObjectVillages = new ArrayList<>();

        for (ObjectVillage objectVillage : allObjectVillages) {
            String key = objectVillage.getObject().getId() + "-" + objectVillage.getDistance();
            if (uniqueCombinations.add(key)) {
                filteredObjectVillages.add(objectVillageToObjectVillageDTO(objectVillage));
            }
        }

        return filteredObjectVillages;
    }

    public void updateObjectVillageStatus(Long id, boolean status, String localDateTime) {
        List<ObjectVillage> objectVillages = objectVillageRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, localDateTime);

        List<ObjectVillage> villa = new ArrayList<>();

        if (!objectVillages.isEmpty()) {
            for (ObjectVillage vill : objectVillages) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setVillageStatus(true);
                vill.setDateDeleted(null);
                villa.add(vill);
            }
            objectVillageRepository.saveAll(villa);
        }
    }

    public void rejectObjectVillageResponse(Long id, boolean status, String responseDate, LocalDateTime dateDelete) {
        List<ObjectVillage> objectVillages = objectVillageRepository.findByVillageIdAndVillageStatusAndDateUpload(
                id, status, responseDate);

        List<ObjectVillage> villa = new ArrayList<>();

        if (!objectVillages.isEmpty()) {
            for (ObjectVillage vill : objectVillages) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setDateDeleted(dateDelete);
                villa.add(vill);
            }
            objectVillageRepository.saveAll(villa);
        }
    }
}
