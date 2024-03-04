package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.RegionDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;

import com.example.ludogorieSoft.village.dtos.*;

import com.example.ludogorieSoft.village.enums.Children;

import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class VillageService {

    private final VillageRepository villageRepository;
    private final ModelMapper modelMapper;
    private final RegionService regionService;
    private final AuthService authService;
    private static final String ERROR_MESSAGE1 = "Village with id ";
    private static final String ERROR_MESSAGE2 = " not found  ";


    public VillageDTO villageToVillageDTO(Village village) {
        return modelMapper.map(village, VillageDTO.class);
    }

    public List<VillageDTO> getAllVillages() {
        List<Village> villages = villageRepository.findAll();
        return villages.stream()
                .map(this::villageToVillageDTO)
                .toList();
    }

    public VillageDTO getVillageById(Long id) {
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            return villageToVillageDTO(optionalVillage.get());
        } else {
            throw new ApiRequestException(ERROR_MESSAGE1 + id + ERROR_MESSAGE2);
        }
    }

    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = villageRepository.findSingleVillageByNameAndRegionName(villageDTO.getName().trim(), villageDTO.getRegion());
        if (village == null) {
            village = new Village();
            village.setName(villageDTO.getName().trim());
            RegionDTO regionDTO = regionService.findRegionByName(villageDTO.getRegion());
            village.setRegion(regionService.checkRegion(regionDTO.getId()));
            village.setStatus(false);
            village.setApprovedResponsesCount(0);
        }
        village.setDateUpload(villageDTO.getDateUpload());
        Village savedVillage = villageRepository.save(village);
        return modelMapper.map(savedVillage, VillageDTO.class);
    }

    public Long createVillageWhitNullValues() {
        Village village = new Village();
        village.setName("null");
        village.setStatus(true);
        village.setDateUpload(LocalDateTime.now());
        village.setDateApproved(LocalDateTime.now());
        villageRepository.save(village);
        return village.getId();
    }


    public VillageDTO updateVillageStatus(Long id, VillageDTO villageDTO) {
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            Village village = optionalVillage.get();
            village.setName(villageDTO.getName());
            RegionDTO regionDTO = regionService.findRegionByName(villageDTO.getRegion());
            village.setRegion(regionService.checkRegion(regionDTO.getId()));

            AdministratorDTO administratorDTO = authService.getAdministratorInfo();

            village.setAdmin(modelMapper.map(administratorDTO, Administrator.class));
            village.setStatus(true);
            village.setDateApproved(now());

            villageRepository.save(village);
            return modelMapper.map(village, VillageDTO.class);
        } else {
            throw new ApiRequestException(ERROR_MESSAGE1 + id + ERROR_MESSAGE2);
        }
    }

    public VillageDTO updateVillage(Long id, VillageDTO villageDTO) {
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            Village village = optionalVillage.get();
            village.setName(villageDTO.getName());
            RegionDTO regionDTO = regionService.findRegionByName(villageDTO.getRegion());
            village.setRegion(regionService.checkRegion(regionDTO.getId()));
            villageRepository.save(village);
            return modelMapper.map(village, VillageDTO.class);
        } else {
            throw new ApiRequestException(ERROR_MESSAGE1 + id + ERROR_MESSAGE2);
        }
    }


    public void deleteVillage(Long id) {
        if (villageRepository.existsById(id)) {
            villageRepository.deleteById(id);
        } else {
            throw new ApiRequestException(ERROR_MESSAGE1 + id + ERROR_MESSAGE2);
        }
    }

    public Village checkVillage(Long id) {
        Optional<Village> village = villageRepository.findById(id);
        if (village.isPresent()) {
            return village.get();
        } else {
            throw new ApiRequestException("Village not found");
        }
    }


    public Page<VillageDTO> getAllSearchVillages(String name, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.findByNameContainingIgnoreCaseOrderByRegionNameAsc(name, page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }


    public Page<VillageDTO> getAllSearchVillagesByRegionName(String regionName, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.findByRegionName(regionName, page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }


    public Page<VillageDTO> getAllSearchVillagesByNameAndRegionName(String regionName, String name, int pageNumber, int elementsCount) {
        Pageable page = PageRequest.of(pageNumber, elementsCount);
        Page<Village> villages = villageRepository.findByNameContainingIgnoreCaseAndRegionName(regionName, name, page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    public Page<VillageDTO> getAllSearchVillagesByNameAndRegionName(String regionName, String name, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.findByNameContainingIgnoreCaseAndRegionName(regionName, name, page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    private Sort getSort(String sort) {
        if ("nameAsc".equals(sort)) {
            return Sort.by(Sort.Order.asc("name"));
        } else if ("nameDesc".equals(sort)) {
            return Sort.by(Sort.Order.desc("name"));
        } else if ("regionAsc".equals(sort)) {
            return Sort.by(Sort.Order.asc("region"));
        } else if ("regionDesc".equals(sort)) {
            return Sort.by(Sort.Order.desc("region"));
        }
        return Sort.unsorted();
    }

    public Page<VillageDTO> getSearchVillages(List<String> objectAroundVillageDTOS, List<String> livingConditionDTOS, Children children, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.searchVillages("region", "name", objectAroundVillageDTOS, livingConditionDTOS, children.getEnumValue(), page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    public Page<VillageDTO> getSearchVillages2(String region, String villageName, List<String> objectAroundVillageDTOS, List<String> livingConditionDTOS, Children children, Pageable pageable) {
        Page<Village> villages = villageRepository.searchVillages(region, villageName, objectAroundVillageDTOS, livingConditionDTOS, children, pageable);
        return villages.map(this::villageToVillageDTO);
    }

    public Page<VillageDTO> getSearchVillagesByLivingConditionAndChildren(List<String> livingConditionDTOS, Children children, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.searchVillagesByLivingConditionAndChildren(livingConditionDTOS, children.getEnumValue(), page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    public Page<VillageDTO> getSearchVillagesByObjectAndChildren(List<String> objectAroundVillageDTOS, Children children, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.searchVillagesByObjectAndChildren(objectAroundVillageDTOS, children.getEnumValue(), page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    public Page<VillageDTO> getSearchVillagesByObjectAndLivingCondition(List<String> objectAroundVillageDTOS, List<String> livingConditionDTOS, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.searchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS, page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    public Page<VillageDTO> getSearchVillagesByChildrenCount(Children children, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.searchVillagesByChildrenCount(children.getEnumValue(), page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    public Page<VillageDTO> getSearchVillagesByObject(List<String> objectAroundVillageDTOS, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.searchVillagesByObject(objectAroundVillageDTOS, page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    public Page<VillageDTO> getSearchVillagesByLivingCondition(List<String> livingConditionDTOS, int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> villages = villageRepository.searchVillagesByLivingCondition(livingConditionDTOS, page);
        return new PageImpl<>(villages.stream().map(this::villageToVillageDTO).toList(), page, villages.getTotalElements());
    }

    public Page<VillageDTO> getAllApprovedVillages(int pageNumber, int elementsCount, String sort) {
        Pageable page = PageRequest.of(pageNumber, elementsCount, getSort(sort));
        Page<Village> approvedVillages = villageRepository.findAllApprovedVillages(page);
        return new PageImpl<>(approvedVillages.stream().map(this::villageToVillageDTO).toList(), page, approvedVillages.getTotalElements());
    }

    public Page<VillageDTO> getVillagesByStatus(boolean status, Pageable page) {
        Page<Village> villagesWithStatusPage = villageRepository.findByStatus(status, page);
        List<VillageDTO> villageDTOsWithStatus = villagesWithStatusPage.getContent()
                .stream()
                .map(village -> modelMapper.map(village, VillageDTO.class))
                .toList();

        return new PageImpl<>(villageDTOsWithStatus, page, villagesWithStatusPage.getTotalElements());
    }

    public void increaseApprovedResponsesCount(Long villageId) {
        Optional<Village> village = villageRepository.findById(villageId);
        if (village.isEmpty()) {
            return;
        }
        village.get().setApprovedResponsesCount(village.get().getApprovedResponsesCount() + 1);
        villageRepository.save(village.get());
    }

    public VillageDTO getVillageByName(String name) {
        List<Village> foundVillages = villageRepository.findByName(name);

        if (foundVillages.isEmpty()) {
            throw new ApiRequestException("Village with name " + name + ERROR_MESSAGE2);
        } else if (foundVillages.size() == 1) {
            return villageToVillageDTO(foundVillages.get(0));
        } else {
            throw new ApiRequestException("There are " + foundVillages.size() + " villages with name " + name);
        }
    }
}
