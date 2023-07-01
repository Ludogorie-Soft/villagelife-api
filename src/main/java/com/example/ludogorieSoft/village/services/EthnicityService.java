package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.EthnicityDTO;
import com.example.ludogorieSoft.village.model.Ethnicity;
import com.example.ludogorieSoft.village.repositories.EthnicityRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EthnicityService {
    private final EthnicityRepository ethnicityRepository;
    private final ModelMapper modelMapper;
    private final  static String ERROR_MESSAGE= "Ethnicity not found";

    public EthnicityDTO ethnicityToEthnicityDTO(Ethnicity ethnicity){
        return modelMapper.map(ethnicity, EthnicityDTO.class);
    }
    public Ethnicity checkEthnicity(Long id){
        Optional<Ethnicity> ethnicity = ethnicityRepository.findById(id);
        if (ethnicity.isPresent()){
            return ethnicity.get();
        }else {
            throw new ApiRequestException(ERROR_MESSAGE);
        }
    }
    public List<EthnicityDTO> getAllEthnicities() {
        List<Ethnicity> ethnicities = ethnicityRepository.findAllByOrderByIdAsc();
        return ethnicities
                .stream()
                .map(this::ethnicityToEthnicityDTO)
                .toList();
    }


    public EthnicityDTO getEthnicityById(Long id) {
        Optional<Ethnicity> ethnicity = ethnicityRepository.findById(id);
        if (ethnicity.isEmpty()) {
            throw new ApiRequestException(ERROR_MESSAGE);
        }
        return ethnicityToEthnicityDTO(ethnicity.get());
    }


    public EthnicityDTO createEthnicity(EthnicityDTO ethnicityDTO) {
        if (StringUtils.isBlank(ethnicityDTO.getEthnicityName())) {
            throw new ApiRequestException("Ethnicity is blank");
        }
        if (ethnicityRepository.existsByEthnicityName(ethnicityDTO.getEthnicityName())) {
            throw new ApiRequestException("Ethnicity with name: " + ethnicityDTO.getEthnicityName() + " already exists");
        }
        Ethnicity ethnicity = new Ethnicity();
        ethnicity.setEthnicityName(ethnicityDTO.getEthnicityName());
        ethnicityRepository.save(ethnicity);
        return ethnicityDTO;
    }

    public EthnicityDTO updateEthnicity(Long id, EthnicityDTO ethnicityDTO) {
        Optional<Ethnicity> findEthnicity = ethnicityRepository.findById(id);
        Ethnicity ethnicity = findEthnicity.orElseThrow(() -> new ApiRequestException(ERROR_MESSAGE));

        if (ethnicityDTO == null || ethnicityDTO.getEthnicityName() == null || ethnicityDTO.getEthnicityName().isEmpty()) {
            throw new ApiRequestException("Invalid ethnicity data");
        }

        String newEthnicity = ethnicityDTO.getEthnicityName();
        if (!newEthnicity.equals(ethnicity.getEthnicityName())) {
            if (ethnicityRepository.existsByEthnicityName(newEthnicity)) {
                throw new ApiRequestException("Ethnicity: " + newEthnicity + " already exists");
            }
            ethnicity.setEthnicityName(newEthnicity);
            ethnicityRepository.save(ethnicity);
        }

        return ethnicityToEthnicityDTO(ethnicity);
    }

    public void deleteEthnicityById(Long id) {
        if (ethnicityRepository.existsById(id)) {
            ethnicityRepository.deleteById(id);
        } else {
            throw new ApiRequestException("Ethnicity with id: " + id + " not found");
        }
    }
    public EthnicityDTO updateEthnicity(Long id, Ethnicity ethnicity) {
        Optional<Ethnicity> foundEthnicity = ethnicityRepository.findById(id);
        if (foundEthnicity.isEmpty()) {
            throw new ApiRequestException(ERROR_MESSAGE);
        }
        foundEthnicity.get().setEthnicityName(ethnicity.getEthnicityName());
        ethnicityRepository.save(foundEthnicity.get());
        return ethnicityToEthnicityDTO(foundEthnicity.get());
    }
    public EthnicityDTO findEthnicityByName(String name){
        Ethnicity ethnicity = ethnicityRepository.findByEthnicityName(name);
        if(ethnicity == null){
            throw new ApiRequestException(ERROR_MESSAGE);
        }
        return ethnicityToEthnicityDTO(ethnicity);
    }

}
