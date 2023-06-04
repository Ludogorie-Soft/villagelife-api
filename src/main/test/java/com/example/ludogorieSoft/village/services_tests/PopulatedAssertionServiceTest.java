package com.example.ludogorieSoft.village.services_tests;

import com.example.ludogorieSoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import com.example.ludogorieSoft.village.repositories.PopulatedAssertionRepository;
import com.example.ludogorieSoft.village.services.PopulatedAssertionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class PopulatedAssertionServiceTest {
    @Mock
    private PopulatedAssertionRepository populatedAssertionRepository;

    @InjectMocks
    private PopulatedAssertionService populatedAssertionService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAllPopulatedAssertionWithPopulations() {
        List<PopulatedAssertion> populations = new ArrayList<>();
        populations.add(new PopulatedAssertion());
        populations.add(new PopulatedAssertion());
        when(populatedAssertionRepository.findAll()).thenReturn(populations);

        List<PopulatedAssertionDTO> result = populatedAssertionService.getAllPopulatedAssertion();

        verify(populatedAssertionRepository, times(1)).findAll();
        Assertions.assertEquals(populations.size(), result.size());
    }

    @Test
    public void testGetAllPopulatedAssertionWithNoPopulations() {
        List<PopulatedAssertion> populations = new ArrayList<>();
        when(populatedAssertionRepository.findAll()).thenReturn(populations);

        List<PopulatedAssertionDTO> result = populatedAssertionService.getAllPopulatedAssertion();

        verify(populatedAssertionRepository, times(1)).findAll();
        Assertions.assertEquals(0, result.size());
    }
    @Test
    public void testCreatePopulatedAssertion() {
        PopulatedAssertion population = new PopulatedAssertion();
        population.setPopulatedAssertionName("Test Population");
        PopulatedAssertionDTO populationDTO = new PopulatedAssertionDTO();
        populationDTO.setPopulatedAssertionName("Test Population");
        when(populatedAssertionRepository.existsByPopulatedAssertionName(population.getPopulatedAssertionName())).thenReturn(false);
        when(populatedAssertionRepository.save(population)).thenReturn(population);
        when(modelMapper.map(population, PopulatedAssertionDTO.class)).thenReturn(populationDTO);

        PopulatedAssertionDTO result = populatedAssertionService.createPopulatedAssertion(populationDTO);
        verify(populatedAssertionRepository, times(1)).existsByPopulatedAssertionName(population.getPopulatedAssertionName());
        verify(populatedAssertionRepository, times(1)).save(population);
        Assertions.assertEquals(populatedAssertionService.toPopulatedAssertionDTO(population), result);
    }
    @Test
    public void testCreatePopulatedAssertionWithExistingPopulation() {
        PopulatedAssertionDTO populationDTO = new PopulatedAssertionDTO();
        populationDTO.setPopulatedAssertionName("Test Population");

        when(populatedAssertionRepository.existsByPopulatedAssertionName(populationDTO.getPopulatedAssertionName())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class, () -> {
            populatedAssertionService.createPopulatedAssertion(populationDTO);
        });

        verify(populatedAssertionRepository, times(1)).existsByPopulatedAssertionName(populationDTO.getPopulatedAssertionName());
        verify(populatedAssertionRepository, never()).save(any(PopulatedAssertion.class));
    }
    @Test
    public void testGetPopulatedAssertionByIdWithExistingId() {
        Long populationId = 123L;
        PopulatedAssertion population = new PopulatedAssertion();
        population.setId(populationId);
        population.setPopulatedAssertionName("Test Population");
        PopulatedAssertionDTO populationDTO = new PopulatedAssertionDTO();
        populationDTO.setId(populationId);
        populationDTO.setPopulatedAssertionName("Test Population");

        Optional<PopulatedAssertion> optionalPopulation = Optional.of(population);

        when(populatedAssertionRepository.findById(populationId)).thenReturn(optionalPopulation);
        when(modelMapper.map(population, PopulatedAssertionDTO.class)).thenReturn(populationDTO);
        PopulatedAssertionDTO result = populatedAssertionService.getPopulatedAssertionById(populationId);

        verify(populatedAssertionRepository, times(1)).findById(populationId);
        Assertions.assertEquals(populatedAssertionService.toPopulatedAssertionDTO(population), result);
    }

    @Test
    public void testGetPopulatedAssertionByIdWithNonExistingId() {
        Long populationId = 123L;

        when(populatedAssertionRepository.findById(populationId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            populatedAssertionService.getPopulatedAssertionById(populationId);
        });

        verify(populatedAssertionRepository, times(1)).findById(populationId);
    }

    @Test
    public void testDeletePopulatedAssertionByIdWithExistingId() {
        Long populationId = 123L;
        PopulatedAssertion population = new PopulatedAssertion();
        population.setId(populationId);

        Optional<PopulatedAssertion> optionalPopulation = Optional.of(population);

        when(populatedAssertionRepository.findById(populationId)).thenReturn(optionalPopulation);
        populatedAssertionService.deletePopulatedAssertionById(populationId);

        verify(populatedAssertionRepository, times(1)).findById(populationId);
        verify(populatedAssertionRepository, times(1)).delete(population);
    }

    @Test
    public void testDeletePopulatedAssertionByIdWithNonExistingId() {
        Long populationId = 123L;

        when(populatedAssertionRepository.findById(populationId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            populatedAssertionService.deletePopulatedAssertionById(populationId);
        });

        verify(populatedAssertionRepository, times(1)).findById(populationId);
        verify(populatedAssertionRepository, never()).delete(any());
    }
//    @Test
//    public void testUpdatePopulatedAssertionWithExistingIdAndNonExistingPopulatedAssertionName() {
//        Long populationId = 123L;
//        PopulatedAssertion population = new PopulatedAssertion();
//        population.setPopulatedAssertionName("Updated Populated Assertion");
//
//        Optional<PopulatedAssertion> optionalPopulation = Optional.of(new PopulatedAssertion());
//        optionalPopulation.get().setPopulatedAssertionName("Old Populated Assertion");
//
//        when(populatedAssertionRepository.findById(populationId)).thenReturn(optionalPopulation);
//        when(populatedAssertionRepository.existsByPopulatedAssertionName(population.getPopulatedAssertionName())).thenReturn(false);
//        when(populatedAssertionRepository.save(any(PopulatedAssertion.class))).thenReturn(population);
//
//        PopulatedAssertionDTO result = populatedAssertionService.updatePopulatedAssertion(populationId, population);
//
//        verify(populatedAssertionRepository, times(1)).findById(populationId);
//        verify(populatedAssertionRepository, times(1)).existsByPopulatedAssertionName(population.getPopulatedAssertionName());
//        verify(populatedAssertionRepository, times(1)).save(any(PopulatedAssertion.class));
//        Assertions.assertEquals(populatedAssertionService.toPopulatedAssertionDTO(population), result);
//    }
//    @Test
//    public void testUpdatePopulatedAssertionWithExistingAssertionName() {
//        Long populationId = 123L;
//        PopulatedAssertion population = new PopulatedAssertion();
//        population.setPopulatedAssertionName("Updated Populated Assertion");
//
//        Optional<PopulatedAssertion> optionalPopulation = Optional.of(new PopulatedAssertion());
//        optionalPopulation.get().setPopulatedAssertionName("Existing Assertion");
//
//        when(populatedAssertionRepository.findById(populationId)).thenReturn(optionalPopulation);
//        when(populatedAssertionRepository.existsByPopulatedAssertionName(population.getPopulatedAssertionName())).thenReturn(true);
//
//        Assertions.assertThrows(ApiRequestException.class, () -> {
//            populatedAssertionService.updatePopulatedAssertion(populationId, population);
//        });
//
//        verify(populatedAssertionRepository, times(1)).findById(populationId);
//        verify(populatedAssertionRepository, times(1)).existsByPopulatedAssertionName(population.getPopulatedAssertionName());
//        verify(populatedAssertionRepository, never()).save(any(PopulatedAssertion.class));
//    }



}
