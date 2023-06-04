package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.EthnicityVillageDTO;
import com.example.ludogorieSoft.village.model.Ethnicity;
import com.example.ludogorieSoft.village.model.EthnicityVillage;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.EthnicityRepository;
import com.example.ludogorieSoft.village.repositories.EthnicityVillageRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class EthnicityVillageService {
    private final ModelMapper modelMapper;
    private final EthnicityVillageRepository ethnicityVillageRepository;
    private final EthnicityRepository ethnicityRepository;
    private final VillageRepository villageRepository;
    private final VillageService villageService;
    private final EthnicityService ethnicityService;

    public EthnicityVillageDTO ethnicityVillageToEthnicityVillageDTO(EthnicityVillage ethnicityVillage) {
        return modelMapper.map(ethnicityVillage, EthnicityVillageDTO.class);
    }

    public List<EthnicityVillageDTO> getAllEthnicityVillages() {
        List<EthnicityVillage> ethnicityVillages = ethnicityVillageRepository.findAll();
        return ethnicityVillages
                .stream()
                .map(this::ethnicityVillageToEthnicityVillageDTO)
                .toList();
    }

    public EthnicityVillageDTO getEthnicityVillageById(Long id) {
        Optional<EthnicityVillage> ethnicityVillage = ethnicityVillageRepository.findById(id);
        if (ethnicityVillage.isEmpty()) {
            throw new ApiRequestException("EthnicityVillage not found");
        }
        return ethnicityVillageToEthnicityVillageDTO(ethnicityVillage.get());
    }

    public EthnicityVillageDTO updateEthnicityVillageById(Long id, EthnicityVillageDTO ethnicityVillage) {
        Optional<EthnicityVillage> foundEthnicityVillage = ethnicityVillageRepository.findById(id);
        if (foundEthnicityVillage.isEmpty()) {
            throw new ApiRequestException("EthnicityVillage not found");
        }
        Optional<Ethnicity> ethnicity = ethnicityRepository.findById(ethnicityVillage.getEthnicityId());
        if (ethnicity.isPresent()){
            foundEthnicityVillage.get().setEthnicity(ethnicity.get());
        }else {
            throw new ApiRequestException("Ethnicity not found");
        }
        Optional<Village> village = villageRepository.findById(ethnicityVillage.getVillageId());
        if (village.isPresent()){
            foundEthnicityVillage.get().setVillage(village.get());
        }else {
            throw new ApiRequestException("Village not found");
        }
        ethnicityVillageRepository.save(foundEthnicityVillage.get());
        return ethnicityVillage;
    }

    public EthnicityVillageDTO createEthnicityVillage(EthnicityVillageDTO ethnicityVillageDTO) {
        EthnicityVillage ethnicityVillage = new EthnicityVillage();

        Village village = villageService.checkVillage(ethnicityVillageDTO.getVillageId());
        ethnicityVillage.setVillage(village);

        Ethnicity ethnicity = ethnicityService.checkEthnicity(ethnicityVillageDTO.getEthnicityId());
        ethnicityVillage.setEthnicity(ethnicity);

        ethnicityVillageRepository.save(ethnicityVillage);
        return ethnicityVillageDTO;
    }

    public int deleteEthnicityVillageById(Long id) {
        try {
            ethnicityVillageRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
}
