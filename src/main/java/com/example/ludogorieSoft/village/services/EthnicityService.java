package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.EthnicityDTO;
import com.example.ludogorieSoft.village.model.Ethnicity;
import com.example.ludogorieSoft.village.repositories.EthnicityRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EthnicityService {
    private final EthnicityRepository ethnicityRepository;
    private final ModelMapper modelMapper;

    public EthnicityDTO ethnicityToEthnicityDTO(Ethnicity ethnicity){
        return modelMapper.map(ethnicity, EthnicityDTO.class);
    }
    public Ethnicity checkEthnicity(Long id){
        Optional<Ethnicity> ethnicity = ethnicityRepository.findById(id);
        if (ethnicity.isPresent()){
            return ethnicity.get();
        }else {
            throw new ApiRequestException("Ethnicity not found");
        }
    }
    public List<EthnicityDTO> getAllEthnicities() {
        List<Ethnicity> ethnicities = ethnicityRepository.findAll();
        return ethnicities
                .stream()
                .map(this::ethnicityToEthnicityDTO)
                .toList();
    }


    public EthnicityDTO getEthnicityById(Long id) {
        Optional<Ethnicity> ethnicity = ethnicityRepository.findById(id);
        if (ethnicity.isEmpty()) {
            throw new ApiRequestException("Ethnicity not found");
        }
        return ethnicityToEthnicityDTO(ethnicity.get());
    }

    public EthnicityDTO createEthnicity(EthnicityDTO ethnicityDTO) {
        if (ethnicityRepository.existsByEthnicityName(ethnicityDTO.getEthnicityName())) {
            throw new ApiRequestException("Ethnicity with name: " + ethnicityDTO.getEthnicityName() + " already exists");
        }

        Ethnicity ethnicity = new Ethnicity();
        ethnicity.setEthnicityName(ethnicityDTO.getEthnicityName());
        ethnicityRepository.save(ethnicity);

        return ethnicityToEthnicityDTO(ethnicity);
    }


    public EthnicityDTO updateEthnicity(Long id, EthnicityDTO ethnicityDTO) {
        Optional<Ethnicity> foundEthnicity = ethnicityRepository.findById(id);
        if (foundEthnicity.isEmpty()) {
            throw new ApiRequestException("Ethnicity not found");
        }
        foundEthnicity.get().setEthnicityName(ethnicityDTO.getEthnicityName());

        ethnicityRepository.save(foundEthnicity.get());
        return ethnicityToEthnicityDTO(foundEthnicity.get());
    }

    public void deleteEthnicityById(Long id) {
        if (ethnicityRepository.existsById(id)) {
            ethnicityRepository.deleteById(id);
        } else {
            throw new ApiRequestException("Ethnicity with id " + id + " not found");
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
