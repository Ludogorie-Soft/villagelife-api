package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.EthnicityVillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.EthnicityVillageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

        ethnicityVillage.setVillageStatus(ethnicityVillageDTO.getStatus());
        ethnicityVillage.setDateUpload(ethnicityVillageDTO.getDateUpload());
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


    public List<EthnicityVillageDTO> getVillageEthnicityByVillageId(Long villageId) {
        List<EthnicityVillage> ethnicityVillages = ethnicityVillageRepository.findAll();

        List<EthnicityVillageDTO> filteredList = new ArrayList<>();

        for (EthnicityVillage ethnicityVillage : ethnicityVillages) {
            if (ethnicityVillage.getVillage().getId().equals(villageId)) {
                filteredList.add(getEthnicityVillageById(ethnicityVillage.getId()));
            }
        }
        return filteredList;
    }

    public List<String> getUniqueEthnicityVillagesByVillageId(Long villageId, boolean status, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = null;
        if (date != null) {
            localDateTime = LocalDateTime.parse(date, formatter);
        }
        List<EthnicityVillage> ethnicityVillages = ethnicityVillageRepository.findAll();
        List<EthnicityVillageDTO> filteredList = new ArrayList<>();
        List<String> ethnicities = new ArrayList<>();
        for (EthnicityVillage ethnicityVillage : ethnicityVillages) {

            if (ethnicityVillage.getVillage().getId().equals(villageId) && !ethnicityVillage.getEthnicity().getEthnicityName().equals("ethnicities.noEthnicities") && (Boolean.TRUE.equals(ethnicityVillage.getVillageStatus()) && status ||
                    Boolean.FALSE.equals(ethnicityVillage.getVillageStatus()) && !status && ethnicityVillage.getDateUpload().equals(localDateTime))) {
                filteredList.add(ethnicityVillageToEthnicityVillageDTO(ethnicityVillage));
            }
        }
        if (filteredList.isEmpty()) {
            ethnicities.add("ethnicities.noEthnicities");
        } else {
            for (int i = 0; i < filteredList.size(); i++) {
                ethnicities.add(ethnicityService.getEthnicityById(filteredList.get(i).getEthnicityId()).getEthnicityName());
            }
        }
        return ethnicities;
    }


    public boolean existsByVillageIdAndEthnicityId(Long villageId, Long ethnicityId) {
        return ethnicityVillageRepository.existsByEthnicityIdAndVillageId(ethnicityId, villageId);
    }

    public void updateEthnicityVillageStatus(Long id, boolean status, String localDateTime) {
        List<EthnicityVillage> ethnicityVillages = ethnicityVillageRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, localDateTime);
        List<EthnicityVillage> villa = new ArrayList<>();

        if (!ethnicityVillages.isEmpty()) {
            for (EthnicityVillage vill : ethnicityVillages) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setVillageStatus(true);
                vill.setDateDeleted(null);
                villa.add(vill);
            }
            ethnicityVillageRepository.saveAll(villa);
        }
    }

    public void rejectEthnicityVillageResponse(Long id, boolean status, String responseDate, LocalDateTime dateDelete) {
        List<EthnicityVillage> ethnicityVillages = ethnicityVillageRepository.findByVillageIdAndVillageStatusAndDateUpload(
                id, status, responseDate
        );
        List<EthnicityVillage> villa = new ArrayList<>();

        if (!ethnicityVillages.isEmpty()) {
            for (EthnicityVillage vill : ethnicityVillages) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setDateDeleted(dateDelete);
                villa.add(vill);
            }
            ethnicityVillageRepository.saveAll(villa);
        }
    }
}
