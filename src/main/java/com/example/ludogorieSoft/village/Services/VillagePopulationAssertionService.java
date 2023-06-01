package com.example.ludogorieSoft.village.Services;

import com.example.ludogorieSoft.village.DTOs.VillagePopulationAssertionDTO;
import com.example.ludogorieSoft.village.Exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.Model.VillagePopulationAssertion;
import com.example.ludogorieSoft.village.Repositories.VillagePopulationAssertionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VillagePopulationAssertionService {
    private final VillagePopulationAssertionRepository villagePopulationAssertionRepository;
    private final ModelMapper modelMapper;
    public VillagePopulationAssertionDTO toDTO(VillagePopulationAssertion villagePopulationAssertion){return modelMapper.map(villagePopulationAssertion,VillagePopulationAssertionDTO.class); }

    public List<VillagePopulationAssertionDTO> getAllVillagePopulationAssertion(){
        List<VillagePopulationAssertion> villagePopulationAssertions=villagePopulationAssertionRepository.findAll();
        return villagePopulationAssertions
                .stream().map(this::toDTO).
                collect(Collectors.toList());
    }
    public VillagePopulationAssertionDTO CreateVillagePopulationAssertionDTO (VillagePopulationAssertion villagePopulationAssertion) {
        villagePopulationAssertionRepository.save(villagePopulationAssertion);
        return toDTO(villagePopulationAssertion);
    }
    public VillagePopulationAssertionDTO getByID(Long id){
        Optional<VillagePopulationAssertion> optionalVillagePopulationAssertion= villagePopulationAssertionRepository.findById(id);
        if (optionalVillagePopulationAssertion.isEmpty()){
            throw new ApiRequestException("VillagePopulationAssertion Not Found ");
        }
        return toDTO(optionalVillagePopulationAssertion.get());
    }

    public int deleteVillagePopulationAssertion(Long id) {
        try {
            villagePopulationAssertionRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

}

