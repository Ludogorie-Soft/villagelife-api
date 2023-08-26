package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.repositories.PopulationRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PopulationService {

    private PopulationRepository populationRepository;
    private final ModelMapper modelMapper;

    public PopulationDTO populationToPopulationDTO(Population population) {
        return modelMapper.map(population, PopulationDTO.class);
    }

    public Population populationDTOtoPopulation(PopulationDTO populationDTO) {
        return modelMapper.map(populationDTO, Population.class);
    }

    public List<PopulationDTO> getAllPopulation() {
        List<Population> populations = populationRepository.findAll();
        return populations
                .stream()
                .map(this::populationToPopulationDTO)
                .toList();
    }

    public PopulationDTO createPopulation(PopulationDTO populationDTO) {
        Population population = populationDTOtoPopulation(populationDTO);
        population.setVillageStatus(populationDTO.getStatus());
        Population savedPopulation = populationRepository.save(population);
        populationDTO.setId(savedPopulation.getId());
        return populationDTO;
    }

    public Long createPopulationWithNullValues() {
        Population population = new Population();
        population.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        populationRepository.save(population);
        return population.getId();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////                                        edit                                        ////
    ////////////////////////////////////////////////////////////////////////////////////////////
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
        findPopulation.get().setPopulationCount(populationDTO.getPopulationCount());
        findPopulation.get().setNumberOfPopulation(populationDTO.getNumberOfPopulation());
        findPopulation.get().setForeigners(populationDTO.getForeigners());
        findPopulation.get().setChildren(populationDTO.getChildren());
        findPopulation.get().setResidents(populationDTO.getResidents());

        populationRepository.save(findPopulation.get());
        return populationToPopulationDTO(findPopulation.get());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////                                        edit                                        ////
    ////////////////////////////////////////////////////////////////////////////////////////////
    public Population findPopulationByVillageNameAndRegion(String name, String regionName) {
        return populationRepository.findByVillageNameAndRegionName(name, regionName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////                                        edit                                        ////
    ////////////////////////////////////////////////////////////////////////////////////////////
    public PopulationDTO findPopulationDTOByVillageNameAndRegion(String name, String regionName) {
        Population population = populationRepository.findByVillageNameAndRegionName(name, regionName);
        if (population == null) {
            throw new ApiRequestException("This population is null!");
        }
        return populationToPopulationDTO(population);
    }

    public PopulationDTO getPopulationByVillageId(Long villageId, boolean status, String date){
        Population population = new Population();

        if (status){
            List<Object[]> foreignersWithCount = populationRepository.countForeignersByVillageIdAndStatusOrderedByCountDesc(villageId);
            population.setForeigners(getForeigners(foreignersWithCount));
            population.setPopulationCount(calculateAveragePopulationCountByVillageId(villageId));

        }else {
            population = populationRepository.findPopulationsByVillageIdAndDateUploadAndStatus(villageId, date, false);
        }

        return populationToPopulationDTO(population);
    }
     public int calculateAveragePopulationCountByVillageId(Long villageId){
        return (int) Math.floor(populationRepository.getAveragePopulationCountByVillageId(villageId));
     }

     public <T extends Enum<T>> List<T> getEnumsWithMaxCount(List<Object[]> rows, Class<T> enumClass) {
         List<T> enums = new ArrayList<>();
         Long maxCount = (Long) rows.get(0)[0];

         for (Object[] row : rows) {
             if (maxCount == row[0]) {
                 enums.add(enumClass.cast(row[1]));
             }
         }

         return enums;
     }

    public Foreigners getForeigners(List<Object[]> rows){
        List<Foreigners> foreigners = getEnumsWithMaxCount(rows, Foreigners.class);
        if(foreigners.size() == 1){
             return foreigners.get(0);
         } else if (foreigners.size() == 2 && foreigners.contains(Foreigners.I_DONT_KNOW)) {
             if(foreigners.get(0) == Foreigners.I_DONT_KNOW){
                 return foreigners.get(1);
             } else {
                 return foreigners.get(0);
             }
         }
         return Foreigners.I_DONT_KNOW;
     }
}
