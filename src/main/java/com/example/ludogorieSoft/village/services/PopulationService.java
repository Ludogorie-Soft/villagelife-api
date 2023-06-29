package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.PopulationDTO;
import com.example.ludogoriesoft.village.model.Population;
import com.example.ludogoriesoft.village.repositories.PopulationRepository;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PopulationService {

    private  PopulationRepository populationRepository;
    private final ModelMapper modelMapper;
    public PopulationDTO populationToPopulationDTO(Population population){
        return modelMapper.map(population, PopulationDTO.class);
    }
    public Population populationDTOtoPopulation(PopulationDTO populationDTO){
        return modelMapper.map(populationDTO, Population.class);
    }

    public List<PopulationDTO> getAllPopulation() {
        List<Population> populations = populationRepository.findAll();
        return populations
                .stream()
                .map(this::populationToPopulationDTO)
                .toList();
    }

    //public PopulationDTO createPopulation(PopulationDTO populationDTO) {
    //    populationRepository.save(populationDTOtoPopulation(populationDTO));
    //    return populationDTO;
    //}
    public PopulationDTO createPopulation(PopulationDTO populationDTO) {
        Population population = populationDTOtoPopulation(populationDTO);
        Population savedPopulation = populationRepository.save(population);
        populationDTO.setId(savedPopulation.getId());
        return populationDTO;
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

    public PopulationDTO updatePopulation(Long id, PopulationDTO populationDTO) {
        Optional<Population> findPopulation = populationRepository.findById(id);
        if (findPopulation.isEmpty()) {
            throw new ApiRequestException("Population not found");
        }
        findPopulation.get().setNumberOfPopulation(populationDTO.getNumberOfPopulation());
        findPopulation.get().setForeigners(populationDTO.getForeigners());
        findPopulation.get().setChildren(populationDTO.getChildren());
        findPopulation.get().setResidents(populationDTO.getResidents());

        populationRepository.save(findPopulation.get());
        return populationToPopulationDTO(findPopulation.get());
    }
}
