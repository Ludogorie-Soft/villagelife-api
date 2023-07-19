package com.example.ludogorieSoft.village.services;
import com.example.ludogorieSoft.village.dtos.VillagePopulationAssertionDTO;
import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.exceptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.PopulatedAssertionRepository;
import com.example.ludogorieSoft.village.repositories.VillagePopulationAssertionRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
        when(villagePopulationAssertionRepository.findAll()).thenReturn(populationAssertions);
        List<VillagePopulationAssertionDTO> result = villagePopulationAssertionService.getAllVillagePopulationAssertion();
        Assertions.assertEquals(2, result.size());
    }


    @Test
    void testGetByIDWhenIdExists() {
        Long id = 1L;
        VillagePopulationAssertion populationAssertion = new VillagePopulationAssertion();
        VillagePopulationAssertionDTO expectedDTO = new VillagePopulationAssertionDTO();
        when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.of(populationAssertion));
        when(villagePopulationAssertionService.toDTO(populationAssertion)).thenReturn(expectedDTO);
        VillagePopulationAssertionDTO result = villagePopulationAssertionService.getByID(id);
        Assertions.assertEquals(expectedDTO, result);
        Mockito.verify(villagePopulationAssertionRepository).findById(id);
    }
    @Test
    void testGetByIDWhenIdDoesNotExist() {
        Long id = 1L;
        when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.empty());
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
    void testDeleteVillagePopulationAssertionWhenIdDoesNotExistShouldReturnZero() {
        Long id = 1L;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(villagePopulationAssertionRepository).deleteById(id);
        int result = villagePopulationAssertionService.deleteVillagePopulationAssertion(id);
        Assertions.assertEquals(0, result);
        Mockito.verify(villagePopulationAssertionRepository).deleteById(id);
    }

    @Test
    void testUpdateVillagePopulationAssertionWhenIdDoesNotExistShouldThrowApiRequestException() {
        Long id = 1L;
        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
        when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class, () -> {
            villagePopulationAssertionService.updateVillagePopulationAssertion(id, inputDTO);
        });
        Mockito.verify(villagePopulationAssertionRepository).findById(id);
    }


    @Test
    void testGetVillagePopulationAssertionByVillageId() {
        Long villageId = 1L;

        Village village = new Village();
        village.setId(villageId);

        VillagePopulationAssertion assertion1 = new VillagePopulationAssertion();
        assertion1.setVillage(village);

        VillagePopulationAssertion assertion2 = new VillagePopulationAssertion();
        assertion2.setVillage(village);

        List<VillagePopulationAssertion> villagePopulationAssertionsList = List.of(assertion1, assertion2);

        when(villagePopulationAssertionRepository.findAll()).thenReturn(villagePopulationAssertionsList);

        List<VillagePopulationAssertionDTO> result = villagePopulationAssertionService.getVillagePopulationAssertionByVillageId(villageId);

        Assertions.assertEquals(2, result.size());
    }


    @Test
    void testUpdateVillagePopulationAssertionWhenIdDoesNotExist() {
        Long id = 1L;
        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();

        when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villagePopulationAssertionService.updateVillagePopulationAssertion(id, inputDTO));

        Mockito.verify(villagePopulationAssertionRepository).findById(id);
    }


    @Test
    void testCreateVillagePopulationAssertionDTOWhenVillageServiceThrowsException() {
        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setPopulatedAssertionId(2L);
        inputDTO.setAnswer(Consents.RATHER_AGREE);

        when(villageService.checkVillage(inputDTO.getVillageId()))
                .thenThrow(new ApiRequestException("Village not found"));

        assertThrows(ApiRequestException.class, () -> {
            villagePopulationAssertionService.createVillagePopulationAssertionDTO(inputDTO);
        });
    }


    @Test
    void testCreateVillagePopulationAssertionDTOWhenPopulatedAssertionServiceThrowsException() {
        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setPopulatedAssertionId(2L);
        inputDTO.setAnswer(Consents.RATHER_AGREE);

        Village village = new Village();
        village.setId(inputDTO.getVillageId());

        when(villageService.checkVillage(inputDTO.getVillageId())).thenReturn(village);

        when(populatedAssertionService.checkPopulatedAssertion(inputDTO.getPopulatedAssertionId()))
                .thenThrow(new ApiRequestException("PopulatedAssertion not found"));

        assertThrows(ApiRequestException.class, () -> {
            villagePopulationAssertionService.createVillagePopulationAssertionDTO(inputDTO);
        });
    }

    @Test
    void testUpdateVillagePopulationAssertionWhenVillageServiceThrowsException() {
        Long id = 1L;
        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setPopulatedAssertionId(2L);
        inputDTO.setAnswer(Consents.RATHER_AGREE);

        when(villageService.checkVillage(inputDTO.getVillageId()))
                .thenThrow(new ApiRequestException("Village not found"));

        assertThrows(ApiRequestException.class, () -> {
            villagePopulationAssertionService.updateVillagePopulationAssertion(id, inputDTO);
        });
    }





}