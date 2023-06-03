package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.repositories.PopulationRepository;
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
public class PopulationService {

    private  PopulationRepository populationRepository;
    private final ModelMapper modelMapper;
    public PopulationDTO populationToPopulationDTO(Population population){
        return modelMapper.map(population, PopulationDTO.class);
    }

    public List<PopulationDTO> getAllPopulation() {
        List<Population> populations = populationRepository.findAll();
        return populations
                .stream()
                .map(this::populationToPopulationDTO)
                .collect(Collectors.toList());
    }

    public PopulationDTO createPopulation(Population population) {
        populationRepository.save(population);
        return populationToPopulationDTO(population);
    }

    public PopulationDTO getPopulationById(Long id) {
        Optional<Population> population = populationRepository.findById(id);
        if (population.isEmpty()) {
            throw new ApiRequestException("This population not found");
        }
        return populationToPopulationDTO(population.get());
    }

    public void deletePopulationById(Long id) {
        Optional<Population> population = populationRepository.findById(id);
        if (population.isEmpty()) {
            throw new ApiRequestException("Population not found for id " + id);
        }
        populationRepository.delete(population.get());
    }

    public PopulationDTO updatePopulation(Long id, Population population) {
        Optional<Population> findPopulation = populationRepository.findById(id);
        if (findPopulation.isEmpty()) {
            throw new ApiRequestException("Population not found");
        }
        findPopulation.get().setNumberOfPopulation(population.getNumberOfPopulation());
        findPopulation.get().setForeigners(population.getForeigners());
        findPopulation.get().setChildren(population.getChildren());
        findPopulation.get().setResidents(population.getResidents());

        populationRepository.save(findPopulation.get());
        return populationToPopulationDTO(findPopulation.get());
    }
}
