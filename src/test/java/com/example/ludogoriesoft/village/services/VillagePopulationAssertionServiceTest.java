package com.example.ludogorieSoft.village.services;

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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VillagePopulationAssertionServiceTest {
    private VillagePopulationAssertionRepository villagePopulationAssertionRepository;
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private PopulatedAssertionRepository populatedAssertionRepository;
    @Mock
    private PopulatedAssertionService populatedAssertionService;
    @Mock
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
        verify(villagePopulationAssertionRepository).findById(id);
    }

    @Test
    void testGetByIDWhenIdDoesNotExist() {
        Long id = 1L;
        when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ApiRequestException.class, () -> villagePopulationAssertionService.getByID(id));
        verify(villagePopulationAssertionRepository).findById(id);
    }

    @Test
    void testDeleteVillagePopulationAssertionWhenIdExists() {
        Long id = 1L;
        Mockito.doNothing().when(villagePopulationAssertionRepository).deleteById(id);
        int result = villagePopulationAssertionService.deleteVillagePopulationAssertion(id);
        Assertions.assertEquals(1, result);
        verify(villagePopulationAssertionRepository).deleteById(id);
    }

    @Test
    void testDeleteVillagePopulationAssertionWhenIdDoesNotExist() {
        Long id = 1L;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(villagePopulationAssertionRepository).deleteById(id);
        int result = villagePopulationAssertionService.deleteVillagePopulationAssertion(id);
        Assertions.assertEquals(0, result);
        verify(villagePopulationAssertionRepository).deleteById(id);
    }

    @Test
    void testUpdateVillagePopulationAssertionWhenIdDoesNotExist() {
        Long id = 1L;
        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
        when(villagePopulationAssertionRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ApiRequestException.class, () -> villagePopulationAssertionService.updateVillagePopulationAssertion(id, inputDTO));
        verify(villagePopulationAssertionRepository).findById(id);
    }

    @Test
    void testGetVillagePopulationAssertionByVillageIdWithValidVillageId() {
        Long villageId = 1L;
        List<VillagePopulationAssertion> villagePopulationAssertionList = new ArrayList<>();
        when(villagePopulationAssertionRepository.findAll()).thenReturn(villagePopulationAssertionList);
        List<VillagePopulationAssertionDTO> result = villagePopulationAssertionService.getVillagePopulationAssertionByVillageId(villageId);
        for (VillagePopulationAssertionDTO dto : result) {
            Assertions.assertEquals(villageId, dto.getVillageId());
        }
    }

    @Test
    void testGetVillagePopulationAssertionByVillageIdWithNullVillageId() {
        Long villageId = null;
        List<VillagePopulationAssertion> villagePopulationAssertionList = new ArrayList<>();
        when(villagePopulationAssertionRepository.findAll()).thenReturn(villagePopulationAssertionList);
        List<VillagePopulationAssertionDTO> result = villagePopulationAssertionService.getVillagePopulationAssertionByVillageId(villageId);
        Assertions.assertEquals(villagePopulationAssertionList.size(), result.size());
    }

    @Test
    void testGetVillagePopulationAssertionByVillageIdWithEmptyList() {
        Long villageId = 1L;
        List<VillagePopulationAssertion> villagePopulationAssertionList = new ArrayList<>();
        when(villagePopulationAssertionRepository.findAll()).thenReturn(villagePopulationAssertionList);
        List<VillagePopulationAssertionDTO> result = villagePopulationAssertionService.getVillagePopulationAssertionByVillageId(villageId);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testGetVillagePopulationAssertionByVillageIdWithNoMatchingVillageId() {
        Long villageId = 2L;
        List<VillagePopulationAssertion> villagePopulationAssertionList = new ArrayList<>();
        when(villagePopulationAssertionRepository.findAll()).thenReturn(villagePopulationAssertionList);
        List<VillagePopulationAssertionDTO> result = villagePopulationAssertionService.getVillagePopulationAssertionByVillageId(villageId);
        Assertions.assertEquals(0, result.size());
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
        Assertions.assertThrows(ApiRequestException.class, () -> villagePopulationAssertionService.updateVillagePopulationAssertion(id, inputDTO));
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
    void testCreateVillagePopulationAssertionDTOWhenVillageServiceThrowsException() {
        VillagePopulationAssertionDTO inputDTO = new VillagePopulationAssertionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setPopulatedAssertionId(2L);
        inputDTO.setAnswer(Consents.RATHER_AGREE);

        when(villageService.checkVillage(inputDTO.getVillageId()))
                .thenThrow(new ApiRequestException("Village not found"));

        assertThrows(ApiRequestException.class, () -> villagePopulationAssertionService.createVillagePopulationAssertionDTO(inputDTO));
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

        assertThrows(ApiRequestException.class, () -> villagePopulationAssertionService.createVillagePopulationAssertionDTO(inputDTO));
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

        assertThrows(ApiRequestException.class, () -> villagePopulationAssertionService.updateVillagePopulationAssertion(id, inputDTO));
    }

    @Test
    void testCreateVillagePopulationAssertionDTO() {
        Long villageId = 1L;
        Long populatedAssertionId = 2L;

        VillagePopulationAssertionDTO villagePopulationAssertionDTO = new VillagePopulationAssertionDTO();
        villagePopulationAssertionDTO.setVillageId(villageId);
        villagePopulationAssertionDTO.setPopulatedAssertionId(populatedAssertionId);
        villagePopulationAssertionDTO.setAnswer(Consents.RATHER_AGREE);

        PopulatedAssertion populatedAssertion = new PopulatedAssertion();
        when(populatedAssertionService.checkPopulatedAssertion(anyLong())).thenReturn(populatedAssertion);

        VillagePopulationAssertionDTO result = villagePopulationAssertionService.createVillagePopulationAssertionDTO(villagePopulationAssertionDTO);

        verify(populatedAssertionService, times(1)).checkPopulatedAssertion(populatedAssertionId);

        verify(villagePopulationAssertionRepository, times(1)).save(any(VillagePopulationAssertion.class));
    }


    @Test
    void testUpdateVillagePopulationAssertion() {
        Long villageId = 1L;
        Long populatedAssertionId = 2L;

        VillagePopulationAssertionDTO villagePopulationAssertionDTO = new VillagePopulationAssertionDTO();
        villagePopulationAssertionDTO.setVillageId(villageId);
        villagePopulationAssertionDTO.setPopulatedAssertionId(populatedAssertionId);
        villagePopulationAssertionDTO.setAnswer(Consents.RATHER_AGREE);

        VillagePopulationAssertion villagePopulationAssertion = new VillagePopulationAssertion();
        villagePopulationAssertion.setId(1L);

        Village village = new Village();
        when(villageService.checkVillage(anyLong())).thenReturn(village);

        PopulatedAssertion populatedAssertion = new PopulatedAssertion();
        when(populatedAssertionService.checkPopulatedAssertion(anyLong())).thenReturn(populatedAssertion);

        when(villagePopulationAssertionRepository.findById(anyLong())).thenReturn(Optional.of(villagePopulationAssertion));
        when(villagePopulationAssertionRepository.save(any(VillagePopulationAssertion.class))).thenReturn(villagePopulationAssertion);

        VillagePopulationAssertionDTO result = villagePopulationAssertionService.updateVillagePopulationAssertion(1L, villagePopulationAssertionDTO);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(populatedAssertionService, times(1)).checkPopulatedAssertion(populatedAssertionId);

        verify(villagePopulationAssertionRepository, times(1)).findById(1L);
        verify(villagePopulationAssertionRepository, times(1)).save(any(VillagePopulationAssertion.class));

      @Test
    void testExistsByVillageIdAndPopulatedAssertionIdAndAnswerWhenRecordExists() {
        Long villageId = 1L;
        Long populatedAssertionId = 10L;
        Consents answer = Consents.COMPLETELY_AGREED;
        when(villagePopulationAssertionRepository.existsByVillageIdAndPopulatedAssertionIDIdAndAnswer(villageId, populatedAssertionId, answer))
                .thenReturn(true);
        boolean result = villagePopulationAssertionService.existsByVillageIdAndPopulatedAssertionIdAndAnswer(villageId, populatedAssertionId, answer);
        Assertions.assertTrue(result);
        verify(villagePopulationAssertionRepository, times(1)).existsByVillageIdAndPopulatedAssertionIDIdAndAnswer(villageId, populatedAssertionId, answer);
    }

    @Test
    void testExistsByVillageIdAndPopulatedAssertionIdAndAnswerWhenRecordDoesNotExist() {
        Long villageId = 1L;
        Long populatedAssertionId = 10L;
        Consents answer = Consents.COMPLETELY_AGREED;
        when(villagePopulationAssertionRepository.existsByVillageIdAndPopulatedAssertionIDIdAndAnswer(villageId, populatedAssertionId, answer))
                .thenReturn(false);
        boolean result = villagePopulationAssertionService.existsByVillageIdAndPopulatedAssertionIdAndAnswer(villageId, populatedAssertionId, answer);
        Assertions.assertFalse(result);
        verify(villagePopulationAssertionRepository, times(1)).existsByVillageIdAndPopulatedAssertionIDIdAndAnswer(villageId, populatedAssertionId, answer);
    }
}
