package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import com.example.ludogorieSoft.village.repositories.PopulatedAssertionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PopulatedAssertionService {

    private final PopulatedAssertionRepository populatedAssertionRepository;
    private final ModelMapper modelMapper;

    public PopulatedAssertionDTO toPopulatedAssertionDTO(PopulatedAssertion populatedAssertion){
        return modelMapper.map(populatedAssertion, PopulatedAssertionDTO.class);
    }

    public List<PopulatedAssertionDTO> getAllPopulatedAssertion() {
        List<PopulatedAssertion> populations = populatedAssertionRepository.findAll();
        return populations
                .stream()
                .map(this::toPopulatedAssertionDTO)
                .toList();
    }

    public PopulatedAssertionDTO createPopulatedAssertion(PopulatedAssertionDTO populatedAssertionDTO) {

        if (populatedAssertionRepository.existsByPopulatedAssertionName(populatedAssertionDTO.getPopulatedAssertionName())) {
            throw new ApiRequestException("PopulatedAssertion with name: " + populatedAssertionDTO.getPopulatedAssertionName() + " already exists");
        }
        PopulatedAssertion populatedAssertion = new PopulatedAssertion();
        populatedAssertion.setPopulatedAssertionName(populatedAssertionDTO.getPopulatedAssertionName());

        populatedAssertionRepository.save(populatedAssertion);

        return toPopulatedAssertionDTO(populatedAssertion);
    }

    public PopulatedAssertionDTO getPopulatedAssertionById(Long id) {
        Optional<PopulatedAssertion> population = populatedAssertionRepository.findById(id);
        if (population.isEmpty()) {
            throw new ApiRequestException("This Populated assertion not found!");
        }
        return toPopulatedAssertionDTO(population.get());
    }

    public void deletePopulatedAssertionById(Long id) {
        Optional<PopulatedAssertion> populatedAssertion = populatedAssertionRepository.findById(id);
        if (populatedAssertion.isEmpty()) {
            throw new ApiRequestException("PopulatedAssertion not found for id " + id);
        }
        populatedAssertionRepository.delete(populatedAssertion.get());
    }

    public PopulatedAssertionDTO updatePopulatedAssertion(Long id, PopulatedAssertionDTO populationDTO) {
        Optional<PopulatedAssertion> findPopulatedAssertion = populatedAssertionRepository.findById(id);
        if (findPopulatedAssertion.isEmpty()) {
            throw new ApiRequestException("Populated Assertion not found!");
        }
        if (populatedAssertionRepository.existsByPopulatedAssertionName(populationDTO.getPopulatedAssertionName())) {
            throw new ApiRequestException("PopulatedAssertion with name: " + populationDTO.getPopulatedAssertionName() + " already exists");
        }
        findPopulatedAssertion.get().setPopulatedAssertionName(populationDTO.getPopulatedAssertionName());
        populatedAssertionRepository.save(findPopulatedAssertion.get());
        return toPopulatedAssertionDTO(findPopulatedAssertion.get());
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
