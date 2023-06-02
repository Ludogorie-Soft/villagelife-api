package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import com.example.ludogorieSoft.village.repositories.PopulatedAssertionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    public PopulatedAssertionDTO createPopulatedAssertion(PopulatedAssertion population) {
        populatedAssertionRepository.save(population);
        return toPopulatedAssertionDTO(population);
    }

    public PopulatedAssertionDTO getPopulatedAssertionById(Long id) {
        Optional<PopulatedAssertion> population = populatedAssertionRepository.findById(id);
        if (population.isEmpty()) {
            throw new ApiRequestException("This Populated assertion not found!");
        }
        return toPopulatedAssertionDTO(population.get());
    }

    public int deletePopulatedAssertionById(Long id) {
        try {
            populatedAssertionRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public PopulatedAssertionDTO updatePopulatedAssertion(Long id, PopulatedAssertion population) {
        Optional<PopulatedAssertion> findPopulatedAssertion = populatedAssertionRepository.findById(id);
        if (findPopulatedAssertion.isEmpty()) {
            throw new ApiRequestException("Populated Assertion not found!");
        }
        findPopulatedAssertion.get().setAssertion(population.getAssertion());


        populatedAssertionRepository.save(findPopulatedAssertion.get());
        return toPopulatedAssertionDTO(findPopulatedAssertion.get());
    }
}
