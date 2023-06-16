package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.EthnicityVillageDTO;
import com.example.ludogoriesoft.village.dtos.VillagePopulationAssertionDTO;
import com.example.ludogoriesoft.village.model.Ethnicity;
import com.example.ludogoriesoft.village.model.EthnicityVillage;
import com.example.ludogoriesoft.village.model.Village;
import com.example.ludogoriesoft.village.model.VillagePopulationAssertion;
import com.example.ludogoriesoft.village.repositories.EthnicityRepository;
import com.example.ludogoriesoft.village.repositories.EthnicityVillageRepository;
import com.example.ludogoriesoft.village.repositories.VillageRepository;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EthnicityVillageService {
    private final ModelMapper modelMapper;
    private final EthnicityVillageRepository ethnicityVillageRepository;
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
        Ethnicity ethnicity = ethnicityService.checkEthnicity(ethnicityVillage.getEthnicityId());
        foundEthnicityVillage.get().setEthnicity(ethnicity);

        Village village = villageService.checkVillage(ethnicityVillage.getVillageId());
        foundEthnicityVillage.get().setVillage(village);

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


    public void deleteEthnicityVillageById(Long id) {
        if (ethnicityVillageRepository.existsById(id)) {
            ethnicityVillageRepository.deleteById(id);
        } else {
            throw new ApiRequestException("Ethnicity in Village with id " + id + " not found");
        }
    }
    public EthnicityVillageDTO getVillageEthnicityByVillageId(Long villageId) {
        List<EthnicityVillage> ethnicityVillages = ethnicityVillageRepository.findAll();

        for (EthnicityVillage ethnicityVillage : ethnicityVillages) {
            if (ethnicityVillage.getVillage().getId().equals(villageId)) {
                return getEthnicityVillageById(ethnicityVillage.getId());
            }
        }
        return new EthnicityVillageDTO();
    }



}
