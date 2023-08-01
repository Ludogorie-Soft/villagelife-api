package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.RegionDTO;
import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;

import com.example.ludogorieSoft.village.enums.Children;

import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public VillageResponse villageToVillageResponse(Village village){
        return modelMapper.map(village, VillageResponse.class);
    }

    public List<VillageDTO> getAllVillages() {
        List<Village> villages = villageRepository.findAll();
        return villages.stream()
                .map(this::villageToVillageDTO)
                .toList();
    }
    public List<VillageResponse> getAllVillagesWithAdmin() {
        List<Village> allVillages = villageRepository.findAll();
        List<VillageResponse> villageResponses = new ArrayList<>();

        for (Village village : allVillages) {
            VillageResponse response = villageToVillageResponse(village);

            if (village.getAdmin() != null) {
                response.setAdmin(modelMapper.map(village.getAdmin(),AdministratorDTO.class));
                response.setDateApproved(village.getDateApproved());
            } else {
                response.setAdmin(null);
                response.setDateApproved(null);
            }

            villageResponses.add(response);
        }

        return villageResponses;
    }

    public VillageDTO getVillageById(Long id) {
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            VillageDTO villageDTO = villageToVillageDTO(optionalVillage.get());
            villageDTO.setPopulationDTO(modelMapper.map(optionalVillage.get().getPopulation(), PopulationDTO.class));
            return villageDTO;
        } else {
            throw new ApiRequestException(ERROR_MESSAGE1 + id + ERROR_MESSAGE2);
        }
    }

    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = villageRepository.findSingleVillageByNameAndRegionName(villageDTO.getName(), villageDTO.getRegion());
        if (village == null) {
            village = new Village();
            village.setName(villageDTO.getName());
            RegionDTO regionDTO = regionService.findRegionByName(villageDTO.getRegion());
            village.setRegion(regionService.checkRegion(regionDTO.getId()));
        }
        village.setPopulation(modelMapper.map(villageDTO.getPopulationDTO(), Population.class));
        village.setPopulationCount(villageDTO.getPopulationCount());
        village.setStatus(villageDTO.getStatus());
        Village savedVillage = villageRepository.save(village);
        return modelMapper.map(savedVillage, VillageDTO.class);
    }

    public Long createVillageWhitNullValues() {
        Village village = new Village();
        village.setName("null");
        village.setPopulationCount(0);
        village.setStatus(true);
        villageRepository.save(village);
        return village.getId();
    }


        public VillageDTO updateVillageStatus(Long id, VillageDTO villageDTO) { // approve village status and set admin and date approved
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            Village village = optionalVillage.get();
            village.setName(villageDTO.getName());
            RegionDTO regionDTO = regionService.findRegionByName(villageDTO.getRegion());
            village.setRegion(regionService.checkRegion(regionDTO.getId()));
            village.setPopulationCount(villageDTO.getPopulationCount());
            village.setPopulation(modelMapper.map(villageDTO.getPopulationDTO(), Population.class));

            AdministratorDTO administratorDTO = authService.getAdministratorInfo();

            if (village.getStatus().equals(true)){
                throw new ApiRequestException("This village already approved!");

            }else {
                village.setAdmin(modelMapper.map(administratorDTO, Administrator.class));
                village.setStatus(true);
                village.setDateApproved(now());
            }

            villageRepository.save(village);
            return modelMapper.map(village, VillageDTO.class);
        } else {
            throw new ApiRequestException(ERROR_MESSAGE1 + id + ERROR_MESSAGE2);
        }
    }

    public VillageDTO updateVillage(Long id, VillageDTO villageDTO) { //this is used to upload villages file
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            Village village = optionalVillage.get();
            village.setName(villageDTO.getName());
            RegionDTO regionDTO = regionService.findRegionByName(villageDTO.getRegion());
            village.setRegion(regionService.checkRegion(regionDTO.getId()));
            village.setPopulationCount(villageDTO.getPopulationCount());
            village.setPopulation(modelMapper.map(villageDTO.getPopulationDTO(), Population.class));
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


    public List<VillageDTO> getAllSearchVillages(String name) {
        List<Village> villages = villageRepository.findByNameContainingIgnoreCaseOrderByRegionNameAsc(name);
        return villages.stream()
                .map(this::villageToVillageDTO)
                .toList();
    }



    public List<VillageDTO> getAllSearchVillagesByRegionName(String regionName) {
        List<Village> villages = villageRepository.findByRegionName(regionName);
        return villages.stream()
                .map(this::villageToVillageDTO)
                .toList();
    }


    public List<VillageDTO> getAllSearchVillagesByNameAndRegionName(String regionName, String name) {
        List<Village> villages = villageRepository.findByNameContainingIgnoreCaseAndRegionName(regionName, name);
        return villages.stream()
                .map(this::villageToVillageDTO)
                .toList();
    }



    public List<VillageDTO> getSearchVillages(List<String> objectAroundVillageDTOS, List<String> livingConditionDTOS, Children children) {
        List<Village> villages = villageRepository.searchVillages(objectAroundVillageDTOS, livingConditionDTOS, children.getEnumValue());
        return convertToDTO(villages);
    }


    public List<VillageDTO> getSearchVillagesByLivingConditionAndChildren(List<String> livingConditionDTOS, Children children) {
        List<Village> villages = villageRepository.searchVillagesByLivingConditionAndChildren(livingConditionDTOS, children.getEnumValue());
        return villageToVillageDTOWithoutObject(villages);
    }

    public List<VillageDTO> getSearchVillagesByObjectAndChildren(List<String> objectAroundVillageDTOS, Children children) {
        List<Village> villages = villageRepository.searchVillagesByObjectAndChildren(objectAroundVillageDTOS, children.getEnumValue());
        return villageToVillageDTOWithoutLivingCondition(villages);
    }

    public List<VillageDTO> getSearchVillagesByObjectAndLivingCondition(List<String> objectAroundVillageDTOS, List<String> livingConditionDTOS) {
        List<Village> villages = villageRepository.searchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS);
        return villageToVillageDTOWithoutChildren(villages);
    }

    public List<VillageDTO> getSearchVillagesByChildrenCount(Children children) {
        List<Village> villages = villageRepository.searchVillagesByChildrenCount(children.getEnumValue());
        return villageToVillageDTOChildren(villages);
    }

    public List<VillageDTO> getSearchVillagesByObject(List<String> objectAroundVillageDTOS) {
        List<Village> villages = villageRepository.searchVillagesByObject(objectAroundVillageDTOS);
        return villageToVillageDTOObject(villages);
    }

    public List<VillageDTO> getSearchVillagesByLivingCondition(List<String> livingConditionDTOS) {
        List<Village> villages = villageRepository.searchVillagesByLivingCondition(livingConditionDTOS);
        return villageToVillageDTOLivingCondition(villages);
    }


    protected List<ObjectAroundVillageDTO> convertToObjectAroundVillageDTOList(List<ObjectVillage> objectVillages) {
        List<ObjectAroundVillageDTO> objectAroundVillageDTOs = new ArrayList<>();

        for (ObjectVillage ov : objectVillages) {
            ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();

            objectAroundVillageDTO.setId(ov.getObject().getId());
            objectAroundVillageDTO.setType(ov.getObject().getType());

            objectAroundVillageDTOs.add(objectAroundVillageDTO);
        }

        return objectAroundVillageDTOs;
    }

    protected List<VillageDTO> villageToVillageDTOObject(List<Village> villages) {
        List<VillageDTO> villageDTOs = new ArrayList<>();

        for (Village village : villages) {
            VillageDTO villageDTO = new VillageDTO();
            villageDTO.setId(village.getId());
            villageDTO.setName(village.getName());
            villageDTO.setRegion(String.valueOf(village.getRegion().getRegionName()));

            villageDTO.setObject(convertToObjectAroundVillageDTOList(village.getObjectVillages()));

            villageDTOs.add(villageDTO);
        }

        return villageDTOs;
    }

    protected List<VillageDTO> villageToVillageDTOLivingCondition(List<Village> villages) {
        List<VillageDTO> villageDTOs = new ArrayList<>();

        for (Village village : villages) {
            VillageDTO villageDTO = new VillageDTO();
            villageDTO.setId(village.getId());
            villageDTO.setName(village.getName());
            villageDTO.setRegion(String.valueOf(village.getRegion().getRegionName()));

            villageDTO.setLivingConditions(convertToLivingConditionDTOList(village.getVillageLivingConditions()));

            villageDTOs.add(villageDTO);
        }

        return villageDTOs;
    }

    protected List<VillageDTO> villageToVillageDTOChildren(List<Village> villages) {
        List<VillageDTO> villageDTOs = new ArrayList<>();

        for (Village village : villages) {
            VillageDTO villageDTO = new VillageDTO();
            villageDTO.setId(village.getId());
            villageDTO.setName(village.getName());
            villageDTO.setRegion(String.valueOf(village.getRegion().getRegionName()));

            villageDTO.setPopulationDTO(convertToPopulationDTO(village.getPopulation().getChildren().getEnumValue()));

            villageDTOs.add(villageDTO);
        }

        return villageDTOs;
    }

    protected List<VillageDTO> villageToVillageDTOWithoutChildren(List<Village> villages) {
        List<VillageDTO> villageDTOs = new ArrayList<>();

        for (Village village : villages) {
            VillageDTO villageDTO = new VillageDTO();
            villageDTO.setId(village.getId());
            villageDTO.setName(village.getName());
            villageDTO.setRegion(String.valueOf(village.getRegion().getRegionName()));

            villageDTO.setObject(convertToObjectAroundVillageDTOList(village.getObjectVillages()));
            villageDTO.setLivingConditions(convertToLivingConditionDTOList(village.getVillageLivingConditions()));

            villageDTOs.add(villageDTO);
        }

        return villageDTOs;
    }

    protected List<VillageDTO> villageToVillageDTOWithoutObject(List<Village> villages) {
        List<VillageDTO> villageDTOs = new ArrayList<>();

        for (Village village : villages) {
            VillageDTO villageDTO = new VillageDTO();
            villageDTO.setId(village.getId());
            villageDTO.setName(village.getName());
            villageDTO.setRegion(String.valueOf(village.getRegion().getRegionName()));

            villageDTO.setLivingConditions(convertToLivingConditionDTOList(village.getVillageLivingConditions()));
            villageDTO.setPopulationDTO(convertToPopulationDTO(village.getPopulation().getChildren().getEnumValue()));

            villageDTOs.add(villageDTO);
        }

        return villageDTOs;
    }

    protected List<VillageDTO> villageToVillageDTOWithoutLivingCondition(List<Village> villages) {
        List<VillageDTO> villageDTOs = new ArrayList<>();

        for (Village village : villages) {
            VillageDTO villageDTO = new VillageDTO();
            villageDTO.setId(village.getId());
            villageDTO.setName(village.getName());
            villageDTO.setRegion(String.valueOf(village.getRegion().getRegionName()));

            villageDTO.setObject(convertToObjectAroundVillageDTOList(village.getObjectVillages()));
            villageDTO.setPopulationDTO(convertToPopulationDTO(village.getPopulation().getChildren().getEnumValue()));

            villageDTOs.add(villageDTO);
        }

        return villageDTOs;
    }

    protected List<VillageDTO> convertToDTO(List<Village> villages) {
        List<VillageDTO> villageDTOs = new ArrayList<>();

        for (Village village : villages) {
            VillageDTO villageDTO = new VillageDTO();
            villageDTO.setId(village.getId());
            villageDTO.setName(village.getName());
            villageDTO.setRegion(String.valueOf(village.getRegion().getRegionName()));

            villageDTO.setObject(convertToObjectAroundVillageDTOList(village.getObjectVillages()));
            villageDTO.setLivingConditions(convertToLivingConditionDTOList(village.getVillageLivingConditions()));
            villageDTO.setPopulationDTO(convertToPopulationDTO(village.getPopulation().getChildren().getEnumValue()));

            villageDTOs.add(villageDTO);
        }

        return villageDTOs;
    }


    protected List<LivingConditionDTO> convertToLivingConditionDTOList(List<VillageLivingConditions> villageLivingConditions) {
        List<LivingConditionDTO> livingConditionDTOs = new ArrayList<>();

        for (VillageLivingConditions vl : villageLivingConditions) {
            LivingConditionDTO livingConditionDTO = new LivingConditionDTO();

            livingConditionDTO.setId(vl.getLivingCondition().getId());
            livingConditionDTO.setLivingConditionName(vl.getLivingCondition().getLivingConditionName());

            livingConditionDTOs.add(livingConditionDTO);
        }

        return livingConditionDTOs;
    }


    protected PopulationDTO convertToPopulationDTO(Children children) {
        Children childrenEnum = children.getEnumValue();
        PopulationDTO populationDTO = new PopulationDTO();

        populationDTO.setChildren(childrenEnum);

        return populationDTO;
    }

    public List<VillageDTO> getAllApprovedVillages() {
        List<Village> approvedVillages = villageRepository.findAllApprovedVillages();
        return approvedVillages.stream()
                .map(this::villageToVillageDTO)
                .toList();
    }

    public List<VillageDTO> getVillagesByStatus(boolean status) {
        List<Village> villagesWithStatus = villageRepository.findByStatus(status);
        List<VillageDTO> villageDTOsWithStatus = new ArrayList<>();
        for (Village village : villagesWithStatus) {
            VillageDTO villageDTO = modelMapper.map(village, VillageDTO.class);
            villageDTOsWithStatus.add(villageDTO);
        }
        return villageDTOsWithStatus;
    }
}
