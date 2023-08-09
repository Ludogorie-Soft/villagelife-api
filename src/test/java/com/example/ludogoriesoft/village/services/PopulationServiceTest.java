package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.enums.Residents;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.repositories.PopulationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class PopulationServiceTest {
    @Mock
    private PopulationRepository populationRepository;

    @InjectMocks
    private PopulationService populationService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreatePopulationWithNullValues() {
        Population population = new Population();
        population.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);

        when(populationRepository.save(any(Population.class))).thenAnswer(invocation -> {
            Population savedPopulation = invocation.getArgument(0);
            savedPopulation.setId(1L);
            return savedPopulation;
        });

        Long resultId = populationService.createPopulationWithNullValues();

        assertNotNull(resultId);
        assertEquals(1L, resultId);

        verify(populationRepository, times(1)).save(any(Population.class));
    }

    @Test
    void testGetPopulationByVillageId_ExistingPopulation() {
        Long villageId = 123L;
        Population population = new Population();
        population.setId(456L);
        population.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);

        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setId(population.getId());
        populationDTO.setNumberOfPopulation(population.getNumberOfPopulation());

        when(populationRepository.findById(villageId)).thenReturn(Optional.of(population));
        when(populationService.populationToPopulationDTO(population)).thenReturn(populationDTO);


        PopulationDTO result = populationService.getPopulationByVillageId(villageId);

        assertEquals(populationDTO.getId(), result.getId());
        assertEquals(populationDTO.getNumberOfPopulation(), result.getNumberOfPopulation());
    }



    @Test
     void testGetPopulationByVillageId_NonExistingPopulation() {
        Long villageId = 123L;
        when(populationRepository.findById(villageId)).thenReturn(Optional.empty());
        assertThrows(ApiRequestException.class, () -> populationService.getPopulationByVillageId(villageId));
    }

    @Test
    void testGetAllPopulationWithPopulations() {
        List<Population> populationList = Arrays.asList(
                new Population(),
                new Population()
        );

        when(populationRepository.findAll()).thenReturn(populationList);

        List<PopulationDTO> result = populationService.getAllPopulation();

        verify(populationRepository, times(1)).findAll();
        Assertions.assertEquals(populationList.size(), result.size());
    }

    @Test
    void testGetAllPopulationWithNoPopulations() {
        List<Population> emptyPopulationList = Collections.emptyList();

        when(populationRepository.findAll()).thenReturn(emptyPopulationList);

        List<PopulationDTO> result = populationService.getAllPopulation();

        verify(populationRepository, times(1)).findAll();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testGetPopulationById() {
        Long populationId = 123L;
        Population population = new Population();
        PopulationDTO populationDTO = new PopulationDTO();

        Optional<Population> optionalPopulation = Optional.of(population);
        when(populationRepository.findById(populationId)).thenReturn(optionalPopulation);
        when(modelMapper.map(population, PopulationDTO.class)).thenReturn(populationDTO);

        PopulationDTO result = populationService.getPopulationById(populationId);

        verify(populationRepository, times(1)).findById(populationId);
        Assertions.assertEquals(populationService.populationToPopulationDTO(population), result);
    }


    @Test
    void testGetPopulationByIdWithNonExistingId() {
        Long populationId = 123L;
        Optional<Population> optionalPopulation = Optional.empty();
        when(populationRepository.findById(populationId)).thenReturn(optionalPopulation);

        Assertions.assertThrows(ApiRequestException.class, () -> populationService.getPopulationById(populationId));

        verify(populationRepository, times(1)).findById(populationId);
    }


    @Test
    void testCreatePopulation() {
        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        populationDTO.setResidents(Residents.UP_TO_2_PERCENT);
        populationDTO.setChildren(Children.BELOW_10);
        populationDTO.setForeigners(Foreigners.YES);

        Population population = new Population();
        population.setNumberOfPopulation(populationDTO.getNumberOfPopulation());
        population.setResidents(populationDTO.getResidents());
        population.setChildren(populationDTO.getChildren());
        population.setForeigners(populationDTO.getForeigners());

        when(populationRepository.save(any(Population.class))).thenReturn(population);
        when(populationService.populationDTOtoPopulation(populationDTO)).thenReturn(population);

        PopulationDTO result = populationService.createPopulation(populationDTO);

        assertNotNull(result);
        assertEquals(populationDTO.getNumberOfPopulation(), result.getNumberOfPopulation());
        assertEquals(populationDTO.getResidents(), result.getResidents());
        assertEquals(populationDTO.getChildren(), result.getChildren());
        assertEquals(populationDTO.getForeigners(), result.getForeigners());

        verify(populationRepository, times(1)).save(any(Population.class));
    }

    @Test
    void testGetPopulationByIdNotFound() {
        Long populationId = 1L;
        when(populationRepository.findById(populationId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> populationService.getPopulationById(populationId));
        verify(populationRepository, times(1)).findById(populationId);
    }


    @Test
    void testDeletePopulationByIdNotFound() {
        Long populationId = 1L;
        when(populationRepository.findById(populationId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> populationService.deletePopulationById(populationId));
        verify(populationRepository, times(1)).findById(populationId);
        verify(populationRepository, never()).delete(any(Population.class));
    }

    @Test
    void testUpdatePopulation() {
        Long populationId = 1L;
        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setNumberOfPopulation(NumberOfPopulation.FROM_51_TO_200_PEOPLE);
        Population population = new Population();
        population.setId(populationId);
        when(populationRepository.findById(populationId)).thenReturn(Optional.of(population));
        when(populationRepository.save(any(Population.class))).thenReturn(population);
        when(modelMapper.map(population, PopulationDTO.class)).thenReturn(populationDTO);

        PopulationDTO result = populationService.updatePopulation(populationId, populationDTO);

        assertNotNull(result);
        assertEquals(populationDTO, result);
        verify(populationRepository, times(1)).findById(populationId);
        verify(populationRepository, times(1)).save(any(Population.class));
    }

    @Test
    void testUpdatePopulationNotFound() {
        Long populationId = 1L;
        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setNumberOfPopulation(NumberOfPopulation.FROM_2000_PEOPLE);
        when(populationRepository.findById(populationId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> populationService.updatePopulation(populationId, populationDTO));
        verify(populationRepository, times(1)).findById(populationId);
        verify(populationRepository, never()).save(any(Population.class));
    }


    @Test
    void testDeletePopulationById() {
        Long populationId = 123L;

        Population population = new Population(populationId, NumberOfPopulation.FROM_11_TO_50_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.NO);
        Optional<Population> optionalPopulation = Optional.of(population);
        when(populationRepository.findById(populationId)).thenReturn(optionalPopulation);

        populationService.deletePopulationById(populationId);

        verify(populationRepository, times(1)).findById(populationId);
        verify(populationRepository, times(1)).delete(population);
    }


    @Test
    void testDeletePopulationByIdWithNonExistingId() {
        Long populationId = 123L;

        Optional<Population> optionalPopulation = Optional.empty();
        when(populationRepository.findById(populationId)).thenReturn(optionalPopulation);

        Assertions.assertThrows(ApiRequestException.class, () -> populationService.deletePopulationById(populationId));
        verify(populationRepository, times(1)).findById(populationId);
        verify(populationRepository, never()).delete(any(Population.class));
    }

    @Test
    void testFindPopulationByVillageNameAndRegionWhenExists() {
        String villageName = "Sample Village";
        String regionName = "Sample Region";
        Population population = new Population(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.I_DONT_KNOW);
        when(populationRepository.findByVillageNameAndRegionName(villageName, regionName)).thenReturn(population);
        Population resultPopulation = populationService.findPopulationByVillageNameAndRegion(villageName, regionName);
        assertEquals(population, resultPopulation);
    }

    @Test
    void testUpdatePopulationWithInvalidId() {
        Long populationId = 1L;

        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setNumberOfPopulation(NumberOfPopulation.FROM_51_TO_200_PEOPLE);

        when(populationRepository.findById(populationId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> populationService.updatePopulation(populationId, populationDTO));

        verify(populationRepository, times(1)).findById(populationId);
        verify(populationRepository, never()).save(any(Population.class));
    }


    @Test
    void testFindPopulationByVillageNameAndRegionWhenNotExists() {
        String villageName = "Nonexistent Village";
        String regionName = "Nonexistent Region";
        when(populationRepository.findByVillageNameAndRegionName(villageName, regionName)).thenReturn(null);
        Population resultPopulation = populationService.findPopulationByVillageNameAndRegion(villageName, regionName);
        assertNull(resultPopulation);
    }

    @Test
    void testFindPopulationDTOByVillageNameAndRegion_Valid() {
        String villageName = "SampleVillage";
        String regionName = "SampleRegion";
        Population mockedPopulation = new Population();
        when(populationRepository.findByVillageNameAndRegionName(villageName, regionName)).thenReturn(mockedPopulation);
        PopulationDTO populationDTO = new PopulationDTO();
        when(populationService.populationToPopulationDTO(mockedPopulation)).thenReturn(populationDTO);
        PopulationDTO result = populationService.findPopulationDTOByVillageNameAndRegion(villageName, regionName);

        assertNotNull(result);
        // Тук може да добавите други проверки според логиката на populationToPopulationDTO
    }

    @Test
    void testFindPopulationDTOByVillageNameAndRegion_PopulationNull() {
        String villageName = "NonExistentVillage";
        String regionName = "NonExistentRegion";
        when(populationRepository.findByVillageNameAndRegionName(villageName, regionName)).thenReturn(null);

        assertThrows(ApiRequestException.class,
                () -> populationService.findPopulationDTOByVillageNameAndRegion(villageName, regionName));
    }
}
