package com.example.ludogoriesoft.village.services;


import com.example.ludogoriesoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import com.example.ludogoriesoft.village.model.PopulatedAssertion;
import com.example.ludogoriesoft.village.repositories.PopulatedAssertionRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PopulatedAssertionService {

    private final PopulatedAssertionRepository populatedAssertionRepository;
    private final ModelMapper modelMapper;

    public PopulatedAssertionDTO populatedAssertionToPopulatedAssertionDTO(PopulatedAssertion populatedAssertion){
        return modelMapper.map(populatedAssertion, PopulatedAssertionDTO.class);
    }

    public List<PopulatedAssertionDTO> getAllPopulatedAssertion() {
        List<PopulatedAssertion> populations = populatedAssertionRepository.findAll();
        return populations
                .stream()
                .map(this::populatedAssertionToPopulatedAssertionDTO)
                .toList();
    }


    public PopulatedAssertionDTO createPopulatedAssertion(PopulatedAssertionDTO populatedAssertionDTO) {
        if (StringUtils.isBlank(populatedAssertionDTO.getPopulatedAssertionName())) {
            throw new ApiRequestException("Populated Assertion is blank");
        }
        if (populatedAssertionRepository.existsByPopulatedAssertionName(populatedAssertionDTO.getPopulatedAssertionName())) {
            throw new ApiRequestException("Populated Assertion: " + populatedAssertionDTO.getPopulatedAssertionName() + " already exists");
        }
        PopulatedAssertion populatedAssertion = new PopulatedAssertion();
        populatedAssertion.setPopulatedAssertionName(populatedAssertionDTO.getPopulatedAssertionName());
        populatedAssertionRepository.save(populatedAssertion);
        return populatedAssertionToPopulatedAssertionDTO(populatedAssertion);
    }

    public PopulatedAssertionDTO getPopulatedAssertionById(Long id) {
        Optional<PopulatedAssertion> population = populatedAssertionRepository.findById(id);
        if (population.isEmpty()) {
            throw new ApiRequestException("This Populated Assertion not found!");
        }
        return populatedAssertionToPopulatedAssertionDTO(population.get());
    }

    public void deletePopulatedAssertionById(Long id) {
        Optional<PopulatedAssertion> populatedAssertion = populatedAssertionRepository.findById(id);
        if (populatedAssertion.isEmpty()) {
            throw new ApiRequestException("Populated Assertion not found for id " + id);
        }
        populatedAssertionRepository.delete(populatedAssertion.get());
    }

    public PopulatedAssertionDTO updatePopulatedAssertion(Long id, PopulatedAssertionDTO populatedAssertionDTO) {
        Optional<PopulatedAssertion> findPopulatedAssertion = populatedAssertionRepository.findById(id);
        PopulatedAssertion populatedAssertion = findPopulatedAssertion.orElseThrow(() -> new ApiRequestException("Populated Assertion not found"));

        if (populatedAssertionDTO == null || populatedAssertionDTO.getPopulatedAssertionName() == null || populatedAssertionDTO.getPopulatedAssertionName().isEmpty()) {
            throw new ApiRequestException("Invalid Populated Assertion data");
        }

        String newPopulatedAssertionName = populatedAssertionDTO.getPopulatedAssertionName();
        if (!newPopulatedAssertionName.equals(populatedAssertion.getPopulatedAssertionName())) {
            if (populatedAssertionRepository.existsByPopulatedAssertionName(newPopulatedAssertionName)) {
                throw new ApiRequestException("Populated Assertion: " + newPopulatedAssertionName + " already exists");
            }
            populatedAssertion.setPopulatedAssertionName(newPopulatedAssertionName);
            populatedAssertionRepository.save(populatedAssertion);
        }

        return populatedAssertionToPopulatedAssertionDTO(populatedAssertion);
    }

    public PopulatedAssertion checkPopulatedAssertion(Long id) {
        Optional<PopulatedAssertion> populatedAssertion = populatedAssertionRepository.findById(id);
        if (populatedAssertion.isPresent()){
            return populatedAssertion.get();
        }else {
            throw new ApiRequestException("Populated Assertion not found");
        }
    }
}
