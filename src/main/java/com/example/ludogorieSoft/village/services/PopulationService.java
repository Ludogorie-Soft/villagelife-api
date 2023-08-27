package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.enums.Residents;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.repositories.PopulationRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PopulationService {

    private PopulationRepository populationRepository;
    private final ModelMapper modelMapper;
    private VillageService villageService;

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

    public Population findPopulationByVillageNameAndRegion(String name, String regionName) {
        return populationRepository.findByVillageNameAndRegionName(name, regionName);
    }

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
            List<Object[]> foreignersWithCount = populationRepository.countForeignersByVillageIdAndStatusTrueOrderedByCountDesc(villageId);
            population.setForeigners(getForeigners(foreignersWithCount));
            List<Object[]> childrenWithCount = populationRepository.countChildrenByVillageIdAndStatusTrueOrderedByCountDesc(villageId);
            population.setChildren(getChildren(childrenWithCount));
            List<Object[]> residentsWithCount = populationRepository.countResidentsByVillageIdAndStatusTrueOrderedByCountDesc(villageId);
            population.setResidents(getResidents(residentsWithCount));
            population.setPopulationCount(calculateAveragePopulationCountByVillageId(villageId));
            population.setNumberOfPopulation(getNumberOfPopulationByPopulationAsNumber(population.getPopulationCount()));
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
            if(foreigners.get(0) != Foreigners.I_DONT_KNOW ||
                    foreigners.get(0) == Foreigners.I_DONT_KNOW &&
                    rows.size() == 3 &&
                    rows.get(1)[0] == rows.get(2)[0] ||
                    rows.size() == 1){
                return foreigners.get(0);
            }else {
                return (Foreigners) rows.get(1)[1];
            }
         } else if (foreigners.size() == 2 && foreigners.contains(Foreigners.I_DONT_KNOW)) {
             if(foreigners.get(0) == Foreigners.I_DONT_KNOW){
                 return foreigners.get(1);
             } else {
                 return foreigners.get(0);
             }
         }
         return Foreigners.I_DONT_KNOW;
     }
     public Children getChildren(List<Object[]> rows){
         List<Children> children = getEnumsWithMaxCount(rows, Children.class);
         int maxValueAsNumber = Integer.MIN_VALUE;
         for(Children childrenResult : children){
             if(childrenResult.getValueAsNumber() > maxValueAsNumber){
                 maxValueAsNumber = childrenResult.getValueAsNumber();
             }
         }
         return Children.getByValueAsNumber(maxValueAsNumber);
     }

    public Residents getResidents(List<Object[]> rows){
        List<Residents> residents = getEnumsWithMaxCount(rows, Residents.class);
        int maxValueAsNumber = Integer.MIN_VALUE;
        for(Residents residentsResult : residents){
            if(residentsResult.getValueAsNumber() > maxValueAsNumber){
                maxValueAsNumber = residentsResult.getValueAsNumber();
            }
        }
        return Residents.getByValueAsNumber(maxValueAsNumber);
    }

    public NumberOfPopulation getNumberOfPopulationByPopulationAsNumber(int populationAsNumber){
        if(populationAsNumber <= 10){
            return NumberOfPopulation.UP_TO_10_PEOPLE;
        }else if(populationAsNumber <= 50){
            return NumberOfPopulation.FROM_11_TO_50_PEOPLE;
        }else if(populationAsNumber <= 200){
            return NumberOfPopulation.FROM_51_TO_200_PEOPLE;
        }else if(populationAsNumber <= 500){
            return NumberOfPopulation.FROM_201_TO_500_PEOPLE;
        }else if (populationAsNumber <= 1000){
            return NumberOfPopulation.FROM_501_TO_1000_PEOPLE;
        }else if(populationAsNumber <= 2000){
            return NumberOfPopulation.FROM_1001_TO_2000_PEOPLE;
        }else {
            return NumberOfPopulation.FROM_2000_PEOPLE;
        }
    }

    public List<Population> findByVillageIdAndVillageStatusDateDeleteNotNull(Long id, boolean status) {
        return populationRepository.findByVillageIdAndVillageStatusAndDateDeleteNotNull(id, status);
    }

    public List<Population> findByVillageIdAndVillageStatus(Long id, boolean status) {
        return populationRepository.findByVillageIdAndVillageStatus(id, status);

    }

    public void updatePopulationStatus(Long villageId, boolean currentStatus, String answerDate) {
        villageService.checkVillage(villageId);
        Population population = populationRepository.findPopulationsByVillageIdAndDateUploadAndStatus(villageId, answerDate, currentStatus);
        population.setVillageStatus(!currentStatus);
        population.setDateDeleted(null);
        populationRepository.save(population);
    }

    public void rejectPopulationResponse(Long villageId, boolean currentStatus, String answerDate, LocalDateTime dateDeleted) {
        villageService.checkVillage(villageId);
        Population population = populationRepository.findPopulationsByVillageIdAndDateUploadAndStatus(villageId, answerDate, currentStatus);
        population.setDateDeleted(dateDeleted);
        populationRepository.save(population);
    }
}
