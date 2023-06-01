package com.example.ludogorieSoft.village.Services;

import com.example.ludogorieSoft.village.DTOs.EthnicityDTO;
import com.example.ludogorieSoft.village.Model.Ethnicity;
import com.example.ludogorieSoft.village.Repositories.EthnicityRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EthnicityService {
    private final EthnicityRepository ethnicityRepository;
    private final ModelMapper modelMapper;

    public EthnicityDTO ethnicityToEthnicityDTO(Ethnicity ethnicity){
        return modelMapper.map(ethnicity, EthnicityDTO.class);
    }

    public List<EthnicityDTO> getAllEthnicities() {
        List<Ethnicity> ethnicities = ethnicityRepository.findAll();
        return ethnicities
                .stream()
                .map(this::ethnicityToEthnicityDTO)
                .collect(Collectors.toList());
    }

    public EthnicityDTO createEthnicity(Ethnicity ethnicity) {
        ethnicityRepository.save(ethnicity);
        return ethnicityToEthnicityDTO(ethnicity);
    }
    public EthnicityDTO getEthnicityById(Long id) {
        Optional<Ethnicity> ethnicity = ethnicityRepository.findById(id);
        if (ethnicity.isEmpty()) {
            throw new ApiRequestException("Ethnicity not found");
        }
        return ethnicityToEthnicityDTO(ethnicity.get());
    }

    public int deleteEthnicityById(Long id) {
        try {
            ethnicityRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
    public EthnicityDTO updateEthnicity(Long id, Ethnicity ethnicity) {
        Optional<Ethnicity> foundEthnicity = ethnicityRepository.findById(id);
        if (foundEthnicity.isEmpty()) {
            throw new ApiRequestException("Administrator not found");
        }
        foundEthnicity.get().setEthnicityName(ethnicity.getEthnicityName());
        ethnicityRepository.save(foundEthnicity.get());
        return ethnicityToEthnicityDTO(foundEthnicity.get());
    }
}
