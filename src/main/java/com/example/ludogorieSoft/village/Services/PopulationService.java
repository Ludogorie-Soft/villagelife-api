package com.example.ludogorieSoft.village.Services;

import com.example.ludogorieSoft.village.DTOs.AdministratorDTO;
import com.example.ludogorieSoft.village.DTOs.PopulationDTO;
import com.example.ludogorieSoft.village.Model.Administrator;
import com.example.ludogorieSoft.village.Model.Population;
import com.example.ludogorieSoft.village.Repositories.PopulationRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    public int deletePopulationById(Long id) {
        try {
            populationRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public PopulationDTO updatePopulation(Long id, Population population) {
        Optional<Population> findPopulation = populationRepository.findById(id);
        if (findPopulation.isEmpty()) {
            throw new ApiRequestException("Population not found");
        }
        findPopulation.get().setNumberOfPopulation(population.getNumberOfPopulation());
        findPopulation.get().setForeigners(population.getForeigners());
        findPopulation.get().setChildrenUpTo14Years(population.getChildrenUpTo14Years());
        findPopulation.get().setResidentsUpTo50Years(population.getResidentsUpTo50Years());

        populationRepository.save(findPopulation.get());
        return populationToPopulationDTO(findPopulation.get());
    }
}
