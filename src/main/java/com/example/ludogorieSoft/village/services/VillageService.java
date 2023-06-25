package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.RegionDTO;
import com.example.ludogoriesoft.village.dtos.LivingConditionDTO;
import com.example.ludogoriesoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogoriesoft.village.dtos.PopulationDTO;
import com.example.ludogoriesoft.village.dtos.VillageDTO;
import com.example.ludogoriesoft.village.enums.Children;

import com.example.ludogoriesoft.village.model.ObjectVillage;
import com.example.ludogoriesoft.village.model.Population;
import com.example.ludogoriesoft.village.model.Village;
import com.example.ludogoriesoft.village.model.VillageLivingConditions;
import com.example.ludogoriesoft.village.repositories.VillageRepository;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class VillageService {

    private final VillageRepository villageRepository;
    private final ModelMapper modelMapper;
    private final RegionService regionService;
    private final String errorMessage1="Village with id ";
    private final String errorMessage2=" not found  ";



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
            throw new ApiRequestException(errorMessage1 + id + errorMessage2);
        }
    }

    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = new Village();
        village.setPopulation(modelMapper.map(villageDTO.getPopulationDTO(), Population.class));
        village.setName(villageDTO.getName());
        RegionDTO regionDTO = regionService.findRegionByName(villageDTO.getRegion());
        village.setRegion(regionService.checkRegion(regionDTO.getId()));
        village.setPopulationCount(villageDTO.getPopulationCount());
        Village savedVillage = villageRepository.save(village);
        villageDTO.setId(savedVillage.getId());
        return modelMapper.map(village, VillageDTO.class);
    }

    public Long createVillageWhitNullValues() {
        Village village = new Village();
        village.setName("null");
        village.setPopulationCount(0);
        village.setStatus(false);
        villageRepository.save(village);
        return village.getId();
    }


    public VillageDTO updateVillage(Long id, VillageDTO villageDTO) {
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
            throw new ApiRequestException(errorMessage1 + id + errorMessage2);
        }
    }


    public void deleteVillage(Long id) {
        if (villageRepository.existsById(id)) {
            villageRepository.deleteById(id);
        } else {
            throw new ApiRequestException(errorMessage1 + id + errorMessage2);
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
        List<Village> villages = villageRepository.findByName(name);
        return villages.stream()
                .map(this::villageToVillageDTO)
                .toList();
    }


    public List<VillageDTO> getAllSearchVillagesByNameAndRegionName(String name, String regionName) {
        List<Village> villages = villageRepository.findByNameAndRegionName(name, regionName);
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


    public List<VillageDTO> getSearchVillages(List<String> objectAroundVillageDTOS, List<String> livingConditionDTOS, Children children) {
        List<Village> villages = villageRepository.searchVillages(objectAroundVillageDTOS, livingConditionDTOS, children.getEnumValue());
        List<VillageDTO> villageDTOs = convertToDTO(villages);
        return villageDTOs;
    }

    public List<VillageDTO> getSearchVillagesByLivingConditionAndChildren(List<String> livingConditionDTOS, Children children) {
        List<Village> villages = villageRepository.searchVillagesByLivingConditionAndChildren(livingConditionDTOS, children.getEnumValue());
        List<VillageDTO> villageDTOs = convertToDTOWithoutObject(villages);
        return villageDTOs;
    }

    public List<VillageDTO> getSearchVillagesByObjectAndChildren(List<String> objectAroundVillageDTOS, Children children) {
        List<Village> villages = villageRepository.searchVillagesByObjectAndChildren(objectAroundVillageDTOS, children.getEnumValue());
        List<VillageDTO> villageDTOs = convertToDTOWithoutLivingCondition(villages);
        return villageDTOs;
    }


    public List<VillageDTO> getSearchVillagesByObjectAndLivingCondition(List<String> objectAroundVillageDTOS, List<String> livingConditionDTOS) {
        List<Village> villages = villageRepository.searchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS);
        List<VillageDTO> villageDTOs = convertToDTOWithoutChildren(villages);
        return villageDTOs;
    }

    private List<VillageDTO> convertToDTOWithoutChildren(List<Village> villages) {
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

    private List<VillageDTO> convertToDTOWithoutObject(List<Village> villages) {
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

    private List<VillageDTO> convertToDTOWithoutLivingCondition(List<Village> villages) {
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

    private List<VillageDTO> convertToDTO(List<Village> villages) {
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

    private List<ObjectAroundVillageDTO> convertToObjectAroundVillageDTOList(List<ObjectVillage> objectVillages) {
        List<ObjectAroundVillageDTO> objectAroundVillageDTOs = new ArrayList<>();

        for (ObjectVillage ov : objectVillages) {
            ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();

            objectAroundVillageDTO.setId(ov.getObject().getId());
            objectAroundVillageDTO.setType(ov.getObject().getType());

            objectAroundVillageDTOs.add(objectAroundVillageDTO);
        }

        return objectAroundVillageDTOs;
    }


    private List<LivingConditionDTO> convertToLivingConditionDTOList(List<VillageLivingConditions> villageLivingConditions) {
        List<LivingConditionDTO> livingConditionDTOs = new ArrayList<>();

        for (VillageLivingConditions vl : villageLivingConditions) {
            LivingConditionDTO livingConditionDTO = new LivingConditionDTO();

            livingConditionDTO.setId(vl.getLivingCondition().getId());
            livingConditionDTO.setLivingConditionName(vl.getLivingCondition().getLivingConditionName());

            livingConditionDTOs.add(livingConditionDTO);
        }

        return livingConditionDTOs;
    }


    private PopulationDTO convertToPopulationDTO(Children children) {
        Children childrenEnum = children.getEnumValue();
        PopulationDTO populationDTO = new PopulationDTO();

        populationDTO.setChildren(childrenEnum);

        return populationDTO;
    }



}

