package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.dtos.VillagePopulationAssertionDTO;
import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.PopulatedAssertionRepository;
import com.example.ludogorieSoft.village.repositories.VillagePopulationAssertionRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class VillagePopulationAssertionServiceTest {

    private VillagePopulationAssertionRepository villagePopulationAssertionRepository;
    private VillageRepository villageRepository;
    private PopulatedAssertionRepository populatedAssertionRepository;
    private PopulatedAssertionService populatedAssertionService;
    private VillageService villageService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VillagePopulationAssertionService villagePopulationAssertionService;

    @BeforeEach
    void setUp() {
        villagePopulationAssertionRepository = Mockito.mock(VillagePopulationAssertionRepository.class);
        villageRepository = Mockito.mock(VillageRepository.class);
        populatedAssertionRepository = Mockito.mock(PopulatedAssertionRepository.class);
        populatedAssertionService = Mockito.mock(PopulatedAssertionService.class);
        villageService = Mockito.mock(VillageService.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        villagePopulationAssertionService = new VillagePopulationAssertionService(
                villagePopulationAssertionRepository,
                villageRepository,
                populatedAssertionRepository,
                populatedAssertionService,
                villageService,
                modelMapper
        );
    }

    @Test
    void testGetAllVillagePopulationAssertion() {
        List<VillagePopulationAssertion> populationAssertions = new ArrayList<>();
        populationAssertions.add(new VillagePopulationAssertion());
        populationAssertions.add(new VillagePopulationAssertion());
        Mockito.when(villagePopulationAssertionRepository.findAll()).thenReturn(populationAssertions);

        List<VillagePopulationAssertionDTO> result = villagePopulationAssertionService.getAllVillagePopulationAssertion();

        Assertions.assertEquals(2, result.size());
    }

//    @Test
//    void testCreateVillagePopulationAssertionDTO() {
//        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
//        inputDTO.setVillageId(1L);
//        inputDTO.setPopulatedAssertionId(2L);
//        inputDTO.setAnswer(Consents.RATHER_AGREE);
//
//        Village village = new Village();
//        village.setId(1L);
//        PopulatedAssertion populatedAssertion = new PopulatedAssertion();
//        populatedAssertion.setId(1L);
//
//        VillagePopulationAssertion createdPopulationAssertion = new VillagePopulationAssertion();
//        Mockito.when(villageService.checkVillage(inputDTO.getVillageId())).thenReturn(village);
//        Mockito.when(populatedAssertionService.checkPopulatedAssertion(inputDTO.getPopulatedAssertionId())).thenReturn(populatedAssertion);
//        Mockito.when(villagePopulationAssertionRepository.save(Mockito.any())).thenReturn(createdPopulationAssertion);
//        Mockito.when(villagePopulationAssertionService.toDTO(createdPopulationAssertion)).thenReturn(inputDTO);
//
//        createdPopulationAssertion.setPopulatedAssertionID(populatedAssertionService.checkPopulatedAssertion(1L));
//        createdPopulationAssertion.setVillage(villageService.checkVillage(1L));
//        createdPopulationAssertion.setAnswer(Consents.RATHER_AGREE);
//
//        VillagePopulationAssertionDTO result = villagePopulationAssertionService.createVillagePopulationAssertionDTO(inputDTO);
//
//        Assertions.assertEquals(inputDTO, result);
//        Mockito.verify(villageRepository).findById(inputDTO.getVillageId());
//        Mockito.verify(populatedAssertionRepository).findById(inputDTO.getPopulatedAssertionId());
//        Mockito.verify(villagePopulationAssertionRepository).save(Mockito.any());
//    }

    @Test
    void testGetByIDWhenIdExists() {
        Long id = 1L;
        VillagePopulationAssertion populationAssertion = new VillagePopulationAssertion();
        VillagePopulationAssertionDTO expectedDTO = new VillagePopulationAssertionDTO();
        Mockito.when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.of(populationAssertion));
        Mockito.when(villagePopulationAssertionService.toDTO(populationAssertion)).thenReturn(expectedDTO);

        VillagePopulationAssertionDTO result = villagePopulationAssertionService.getByID(id);

        Assertions.assertEquals(expectedDTO, result);
        Mockito.verify(villagePopulationAssertionRepository).findById(id);
    }

    @Test
    void testGetByIDWhenIdDoesNotExist() {
        Long id = 1L;
        Mockito.when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            villagePopulationAssertionService.getByID(id);
        });
        Mockito.verify(villagePopulationAssertionRepository).findById(id);
    }

    @Test
    void testDeleteVillagePopulationAssertionWhenIdExists() {
        Long id = 1L;
        Mockito.doNothing().when(villagePopulationAssertionRepository).deleteById(id);

        int result = villagePopulationAssertionService.deleteVillagePopulationAssertion(id);

        Assertions.assertEquals(1, result);
        Mockito.verify(villagePopulationAssertionRepository).deleteById(id);
    }

    @Test
    void testDeleteVillagePopulationAssertionWhenIdDoesNotExist() {
        Long id = 1L;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(villagePopulationAssertionRepository).deleteById(id);

        int result = villagePopulationAssertionService.deleteVillagePopulationAssertion(id);

        Assertions.assertEquals(0, result);
        Mockito.verify(villagePopulationAssertionRepository).deleteById(id);
    }

//    @Test
//    void testUpdateVillagePopulationAssertionWhenIdExists() {
//        Long id = 1L;
//        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
//        inputDTO.setVillageId(1L);
//        inputDTO.setPopulatedAssertionId(2L);
//        inputDTO.setAnswer(Consents.COMPLETELY_AGREED);
//
//        Village village = new Village();
//        PopulatedAssertion populatedAssertion = new PopulatedAssertion();
//        VillagePopulationAssertion foundPopulationAssertion = new VillagePopulationAssertion();
//        VillagePopulationAssertion updatedPopulationAssertion = new VillagePopulationAssertion();
//        Mockito.when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.of(foundPopulationAssertion));
//        Mockito.when(villageService.checkVillage(inputDTO.getVillageId())).thenReturn(village);
//        Mockito.when(populatedAssertionService.checkPopulatedAssertion(inputDTO.getPopulatedAssertionId())).thenReturn(populatedAssertion);
//        Mockito.when(villagePopulationAssertionRepository.save(Mockito.any(VillagePopulationAssertion.class))).thenReturn(updatedPopulationAssertion);
//        Mockito.when(villagePopulationAssertionService.toDTO(updatedPopulationAssertion)).thenReturn(inputDTO);
//
//        VillagePopulationAssertionDTO result = villagePopulationAssertionService.updateVillagePopulationAssertion(id, inputDTO);
//
//        Assertions.assertEquals(inputDTO, result);
//        Mockito.verify(villagePopulationAssertionRepository).findById(id);
//        Mockito.verify(villageService).checkVillage(inputDTO.getVillageId());
//        Mockito.verify(populatedAssertionService).checkPopulatedAssertion(inputDTO.getPopulatedAssertionId());
//        Mockito.verify(villagePopulationAssertionRepository).save(Mockito.any(VillagePopulationAssertion.class));
//    }


    @Test
    void testUpdateVillagePopulationAssertionWhenIdDoesNotExist() {
        Long id = 1L;
        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
        Mockito.when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            villagePopulationAssertionService.updateVillagePopulationAssertion(id, inputDTO);
        });
        Mockito.verify(villagePopulationAssertionRepository).findById(id);
    }
}
