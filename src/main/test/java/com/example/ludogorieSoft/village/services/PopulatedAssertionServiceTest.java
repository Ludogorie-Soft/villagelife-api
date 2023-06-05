package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import com.example.ludogorieSoft.village.repositories.PopulatedAssertionRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PopulatedAssertionServiceTest {
    @Mock
    private PopulatedAssertionRepository populatedAssertionRepository;

    @InjectMocks
    private PopulatedAssertionService populatedAssertionService;
    @Mock
    private ModelMapper modelMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        populatedAssertionService = new PopulatedAssertionService(populatedAssertionRepository, modelMapper);
    }

    @Test
    void testGetAllPopulatedAssertionWithPopulations() {
        List<PopulatedAssertion> populations = new ArrayList<>();
        populations.add(new PopulatedAssertion());
        populations.add(new PopulatedAssertion());
        when(populatedAssertionRepository.findAll()).thenReturn(populations);

        List<PopulatedAssertionDTO> result = populatedAssertionService.getAllPopulatedAssertion();

        verify(populatedAssertionRepository, times(1)).findAll();
        Assertions.assertEquals(populations.size(), result.size());
    }

    @Test
    void testGetAllPopulatedAssertionWithNoPopulations() {
        List<PopulatedAssertion> populations = new ArrayList<>();
        when(populatedAssertionRepository.findAll()).thenReturn(populations);

        List<PopulatedAssertionDTO> result = populatedAssertionService.getAllPopulatedAssertion();

        verify(populatedAssertionRepository, times(1)).findAll();
        Assertions.assertEquals(0, result.size());
    }


    @Test
    void testCreatePopulatedAssertion() {
        String populatedAssertionName = "Test Populated Assertion";
        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setPopulatedAssertionName(populatedAssertionName);

        when(populatedAssertionRepository.existsByPopulatedAssertionName(populatedAssertionName)).thenReturn(false);
        when(populatedAssertionRepository.save(any(PopulatedAssertion.class))).thenAnswer(invocation -> {
            PopulatedAssertion savedPopulatedAssertion = invocation.getArgument(0);
            savedPopulatedAssertion.setId(1L);
            return savedPopulatedAssertion;
        });

        PopulatedAssertionDTO createdPopulatedAssertion = populatedAssertionService.createPopulatedAssertion(populatedAssertionDTO);

        assertNotNull(createdPopulatedAssertion);
        assertEquals(populatedAssertionName, createdPopulatedAssertion.getPopulatedAssertionName());
        assertNotNull(createdPopulatedAssertion.getId());
        verify(populatedAssertionRepository, times(1)).existsByPopulatedAssertionName(populatedAssertionName);
        verify(populatedAssertionRepository, times(1)).save(any(PopulatedAssertion.class));
    }

    @Test
    void testCreatePopulatedAssertionWithExistingPopulation() {
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
    void testGetPopulatedAssertionByIdWithExistingId() {
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
    void testGetPopulatedAssertionByIdWithNonExistingId() {
        Long populationId = 123L;

        when(populatedAssertionRepository.findById(populationId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            populatedAssertionService.getPopulatedAssertionById(populationId);
        });

        verify(populatedAssertionRepository, times(1)).findById(populationId);
    }


    @Test
    void testCreatePopulatedAssertionAlreadyExists() {
        String populatedAssertionName = "Test Populated Assertion";
        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setPopulatedAssertionName(populatedAssertionName);

        when(populatedAssertionRepository.existsByPopulatedAssertionName(populatedAssertionName)).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> {
            populatedAssertionService.createPopulatedAssertion(populatedAssertionDTO);
        });

        verify(populatedAssertionRepository, times(1)).existsByPopulatedAssertionName(populatedAssertionName);
        verify(populatedAssertionRepository, never()).save(any(PopulatedAssertion.class));
    }

    @Test
    void testUpdatePopulatedAssertion() {
        Long populatedAssertionId = 1L;
        String updatedName = "Updated Populated Assertion";
        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setPopulatedAssertionName(updatedName);

        PopulatedAssertion existingPopulatedAssertion = new PopulatedAssertion();
        existingPopulatedAssertion.setId(populatedAssertionId);
        existingPopulatedAssertion.setPopulatedAssertionName("Old Populated Assertion");

        when(populatedAssertionRepository.findById(populatedAssertionId)).thenReturn(Optional.of(existingPopulatedAssertion));
        when(populatedAssertionRepository.existsByPopulatedAssertionName(updatedName)).thenReturn(false);
        when(populatedAssertionRepository.save(existingPopulatedAssertion)).thenReturn(existingPopulatedAssertion);

        PopulatedAssertionDTO updatedPopulatedAssertion = populatedAssertionService.updatePopulatedAssertion(populatedAssertionId, populatedAssertionDTO);

        assertNotNull(updatedPopulatedAssertion);
        assertEquals(updatedName, updatedPopulatedAssertion.getPopulatedAssertionName());
        assertEquals(populatedAssertionId, updatedPopulatedAssertion.getId());
        verify(populatedAssertionRepository, times(1)).findById(populatedAssertionId);
        verify(populatedAssertionRepository, times(1)).existsByPopulatedAssertionName(updatedName);
        verify(populatedAssertionRepository, times(1)).save(existingPopulatedAssertion);
    }

    @Test
    void testUpdatePopulatedAssertionInvalidPopulatedAssertionId() {
        Long invalidPopulatedAssertionId = 100L;
        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setPopulatedAssertionName("Updated Populated Assertion");

        when(populatedAssertionRepository.findById(invalidPopulatedAssertionId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> {
            populatedAssertionService.updatePopulatedAssertion(invalidPopulatedAssertionId, populatedAssertionDTO);
        });

        verify(populatedAssertionRepository, times(1)).findById(invalidPopulatedAssertionId);
        verify(populatedAssertionRepository, never()).existsByPopulatedAssertionName(anyString());
        verify(populatedAssertionRepository, never()).save(any(PopulatedAssertion.class));
    }

    @Test
    void testUpdatePopulatedAssertionAlreadyExists() {
        Long populatedAssertionId = 1L;
        String updatedName = "Updated Populated Assertion";
        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setPopulatedAssertionName(updatedName);

        PopulatedAssertion existingPopulatedAssertion = new PopulatedAssertion();
        existingPopulatedAssertion.setId(populatedAssertionId);
        existingPopulatedAssertion.setPopulatedAssertionName("Old Populated Assertion");

        when(populatedAssertionRepository.findById(populatedAssertionId)).thenReturn(Optional.of(existingPopulatedAssertion));
        when(populatedAssertionRepository.existsByPopulatedAssertionName(updatedName)).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> {
            populatedAssertionService.updatePopulatedAssertion(populatedAssertionId, populatedAssertionDTO);
        });

        verify(populatedAssertionRepository, times(1)).findById(populatedAssertionId);
        verify(populatedAssertionRepository, times(1)).existsByPopulatedAssertionName(updatedName);
        verify(populatedAssertionRepository, never()).save(any(PopulatedAssertion.class));
    }

    @Test
    void testDeletePopulatedAssertionByIdWithExistingId() {
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
    void testDeletePopulatedAssertionByIdWithNonExistingId() {
        Long populationId = 123L;

        when(populatedAssertionRepository.findById(populationId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            populatedAssertionService.deletePopulatedAssertionById(populationId);
        });

        verify(populatedAssertionRepository, times(1)).findById(populationId);
        verify(populatedAssertionRepository, never()).delete(any());
    }

}
