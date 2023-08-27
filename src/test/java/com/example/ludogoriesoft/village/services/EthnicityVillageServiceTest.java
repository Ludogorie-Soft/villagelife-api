package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.EthnicityVillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Ethnicity;
import com.example.ludogorieSoft.village.model.EthnicityVillage;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.EthnicityVillageRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Collections;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class EthnicityVillageServiceTest {
    @Captor
    private ArgumentCaptor<List<EthnicityVillage>> ethnicityVillageListCaptor;
    @Mock
    private EthnicityVillageRepository ethnicityVillageRepository;
    @InjectMocks
    private EthnicityVillageService ethnicityVillageService;

    private VillageService villageService;
    private EthnicityService ethnicityService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        ethnicityVillageRepository = mock(EthnicityVillageRepository.class);
        villageService = mock(VillageService.class);
        ethnicityService = mock(EthnicityService.class);
        modelMapper = mock(ModelMapper.class);
        ethnicityVillageService = new EthnicityVillageService(modelMapper, ethnicityVillageRepository, villageService, ethnicityService);
    }

    @Test
    void deleteEthnicityVillageById_ShouldDeleteWhenIdExists() {
        Long id = 1L;
        EthnicityVillageRepository ethnicityVillageRepository = mock(EthnicityVillageRepository.class);

        when(ethnicityVillageRepository.existsById(id)).thenReturn(true);

        EthnicityVillageService ethnicityVillageService = new EthnicityVillageService(modelMapper, ethnicityVillageRepository, villageService, ethnicityService);
        ethnicityVillageService.deleteEthnicityVillageById(id);

        verify(ethnicityVillageRepository, times(1)).existsById(id);
        verify(ethnicityVillageRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllEthnicityVillages() {
        EthnicityVillage ethnicityVillage1 = new EthnicityVillage();
        ethnicityVillage1.setId(1L);
        EthnicityVillage ethnicityVillage2 = new EthnicityVillage();
        ethnicityVillage2.setId(2L);
        List<EthnicityVillage> ethnicityVillages = new ArrayList<>();
        ethnicityVillages.add(ethnicityVillage1);
        ethnicityVillages.add(ethnicityVillage2);

        EthnicityVillageDTO ethnicityVillageDTO1 = new EthnicityVillageDTO();
        ethnicityVillageDTO1.setId(1L);
        EthnicityVillageDTO ethnicityVillageDTO2 = new EthnicityVillageDTO();
        ethnicityVillageDTO2.setId(2L);
        List<EthnicityVillageDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(ethnicityVillageDTO1);
        expectedDTOs.add(ethnicityVillageDTO2);

        when(ethnicityVillageRepository.findAll()).thenReturn(ethnicityVillages);
        when(modelMapper.map(any(EthnicityVillage.class), eq(EthnicityVillageDTO.class)))
                .thenReturn(ethnicityVillageDTO1)
                .thenReturn(ethnicityVillageDTO2);

        List<EthnicityVillageDTO> resultDTOs = ethnicityVillageService.getAllEthnicityVillages();

        assertEquals(expectedDTOs.size(), resultDTOs.size());
        assertEquals(expectedDTOs.get(0).getId(), resultDTOs.get(0).getId());
        assertEquals(expectedDTOs.get(1).getId(), resultDTOs.get(1).getId());
    }

    @Test
    void testGetEthnicityVillageByIdWhenEthnicityVillageExists() {
        Long id = 1L;
        EthnicityVillage ethnicityVillage = new EthnicityVillage();
        ethnicityVillage.setId(id);
        EthnicityVillageDTO expectedDTO = new EthnicityVillageDTO();
        expectedDTO.setId(id);

        when(ethnicityVillageRepository.findById(id)).thenReturn(Optional.of(ethnicityVillage));
        when(modelMapper.map(ethnicityVillage, EthnicityVillageDTO.class)).thenReturn(expectedDTO);

        EthnicityVillageDTO resultDTO = ethnicityVillageService.getEthnicityVillageById(id);

        assertEquals(expectedDTO.getId(), resultDTO.getId());
    }

    @Test
    void testGetEthnicityVillageByIdWhenEthnicityVillageDoesNotExist() {
        Long id = 1L;
        when(ethnicityVillageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> ethnicityVillageService.getEthnicityVillageById(id));
    }

    @Test
    void testUpdateEthnicityVillageByIdWhenEthnicityVillageExists() {
        Long id = 1L;
        EthnicityVillageDTO ethnicityVillageDTO = new EthnicityVillageDTO();
        ethnicityVillageDTO.setEthnicityId(1L);
        ethnicityVillageDTO.setVillageId(1L);

        EthnicityVillage ethnicityVillage = new EthnicityVillage();
        ethnicityVillage.setId(id);

        Ethnicity ethnicity = new Ethnicity();
        ethnicity.setId(1L);

        Village village = new Village();
        village.setId(1L);

        when(ethnicityVillageRepository.findById(id)).thenReturn(Optional.of(ethnicityVillage));
        when(ethnicityService.checkEthnicity(ethnicityVillageDTO.getEthnicityId())).thenReturn(ethnicity);
        when(villageService.checkVillage(ethnicityVillageDTO.getVillageId())).thenReturn(village);
        when(ethnicityVillageRepository.save(ethnicityVillage)).thenReturn(ethnicityVillage);

        EthnicityVillageDTO resultDTO = ethnicityVillageService.updateEthnicityVillageById(id, ethnicityVillageDTO);

        assertEquals(ethnicityVillageDTO, resultDTO);
    }

    @Test
    void testUpdateEthnicityVillageByIdWhenEthnicityVillageDoesNotExist() {
        Long id = 1L;
        EthnicityVillageDTO ethnicityVillageDTO = new EthnicityVillageDTO();
        when(ethnicityVillageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> ethnicityVillageService.updateEthnicityVillageById(id, ethnicityVillageDTO));
    }

    @Test
    void testCreateEthnicityVillage() {
        EthnicityVillageDTO ethnicityVillageDTO = new EthnicityVillageDTO();
        ethnicityVillageDTO.setEthnicityId(1L);
        ethnicityVillageDTO.setVillageId(1L);

        Village village = new Village();
        village.setId(1L);

        Ethnicity ethnicity = new Ethnicity();
        ethnicity.setId(1L);

        when(villageService.checkVillage(ethnicityVillageDTO.getVillageId())).thenReturn(village);
        when(ethnicityService.checkEthnicity(ethnicityVillageDTO.getEthnicityId())).thenReturn(ethnicity);
        when(ethnicityVillageRepository.save(any(EthnicityVillage.class))).thenReturn(new EthnicityVillage());

        EthnicityVillageDTO resultDTO = ethnicityVillageService.createEthnicityVillage(ethnicityVillageDTO);

        assertNotNull(resultDTO);
    }


    @Test
    void testDeleteEthnicityVillageByIdWhenEthnicityVillageDoesNotExist() {
        Long id = 1L;
        when(ethnicityVillageRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ApiRequestException.class,
                () -> ethnicityVillageService.deleteEthnicityVillageById(id));
        verify(ethnicityVillageRepository, times(1)).existsById(id);
        verify(ethnicityVillageRepository, never()).deleteById(id);
    }


    @Test
    void testDeleteEthnicityVillageByIdWhenEmptyResultDataAccessExceptionThrown() {
        lenient().doThrow(EmptyResultDataAccessException.class).when(ethnicityVillageRepository).deleteById(anyLong());

        assertThrows(ApiRequestException.class, () -> ethnicityVillageService.deleteEthnicityVillageById(1L));
    }


    @Test
    void testDeleteEthnicityVillageByIdWhenEthnicityVillageExists() {
        Long id = 1L;

        when(ethnicityVillageRepository.existsById(id)).thenReturn(true);

        ethnicityVillageService.deleteEthnicityVillageById(id);

        verify(ethnicityVillageRepository, times(1)).deleteById(id);
    }


    @Test
    void testGetVillageEthnicityByVillageIdWithEmptyList() {
        Long villageId = 1L;

        when(ethnicityVillageRepository.findAll()).thenReturn(Collections.emptyList());

        List<EthnicityVillageDTO> resultDTOs = ethnicityVillageService.getVillageEthnicityByVillageId(villageId);

        assertTrue(resultDTOs.isEmpty());
    }


    @Test
    void testGetVillageEthnicityByVillageIdWithEmptyEthnicityVillages() {
        Long villageId = 1L;

        when(ethnicityVillageRepository.findAll()).thenReturn(Collections.emptyList());

        List<EthnicityVillageDTO> resultDTOs = ethnicityVillageService.getVillageEthnicityByVillageId(villageId);

        assertTrue(resultDTOs.isEmpty());
        assertThrows(ApiRequestException.class, () -> ethnicityVillageService.deleteEthnicityVillageById(1L));
    }

    @Test
    void testExistsByVillageIdAndEthnicityIdWhenExists() {
        Long villageId = 1L;
        Long ethnicityId = 2L;
        when(ethnicityVillageRepository.existsByEthnicityIdAndVillageId(ethnicityId, villageId)).thenReturn(true);
        boolean exists = ethnicityVillageService.existsByVillageIdAndEthnicityId(villageId, ethnicityId);
        assertTrue(exists);
    }

    @Test
    void testExistsByVillageIdAndEthnicityIdNotExists() {
        Long villageId = 1L;
        Long ethnicityId = 2L;
        when(ethnicityVillageRepository.existsByEthnicityIdAndVillageId(ethnicityId, villageId)).thenReturn(false);
        boolean exists = ethnicityVillageService.existsByVillageIdAndEthnicityId(villageId, ethnicityId);
        assertFalse(exists);
    }

    @Test
    void testGetUniqueEthnicityVillagesByVillageIdWithNoEthnicities() {
        Long villageId = 1L;
        when(ethnicityVillageRepository.findAll()).thenReturn(new ArrayList<>());

        String result = ethnicityVillageService.getUniqueEthnicityVillagesByVillageId(villageId, true, null);

        verify(ethnicityVillageRepository, times(1)).findAll();
        assertEquals("\u043D\u044F\u043C\u0430 \u043C\u0430\u043B\u0446\u0438\u043D\u0441\u0442\u0432\u0435\u043D\u0438 \u0433\u0440\u0443\u043F\u0438", result);
    }

    @Test
    void testUpdateEthnicityVillageStatus() {
        Long villageId = 1L;
        String localDateTime = "2023-08-10T00:00:00";
        boolean status = true;

        Village village = new Village();
        village.setId(villageId);

        EthnicityVillage ethnicityVillage = new EthnicityVillage();
        ethnicityVillage.setVillage(village);

        List<EthnicityVillage> ethnicityVillages = new ArrayList<>();
        ethnicityVillages.add(ethnicityVillage);

        when(ethnicityVillageRepository.findByVillageIdAndVillageStatusAndDateUpload(
                villageId, status, localDateTime
        )).thenReturn(ethnicityVillages);

        ethnicityVillageService.updateEthnicityVillageStatus(villageId, status, localDateTime);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(ethnicityVillageRepository).saveAll(ethnicityVillageListCaptor.capture());
    }

    @Test
    void testRejectEthnicityVillageResponse() {
        Long villageId = 1L;
        String responseDate = "2023-08-10T00:00:00";
        LocalDateTime deleteDate = LocalDateTime.now();

        Village village = new Village();
        village.setId(villageId);

        EthnicityVillage ethnicityVillage = new EthnicityVillage();
        ethnicityVillage.setVillage(village);

        List<EthnicityVillage> ethnicityVillages = new ArrayList<>();
        ethnicityVillages.add(ethnicityVillage);

        when(ethnicityVillageRepository.findByVillageIdAndVillageStatusAndDateUpload(
                villageId, true, responseDate
        )).thenReturn(ethnicityVillages);

        ethnicityVillageService.rejectEthnicityVillageResponse(villageId, true, responseDate, deleteDate);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(ethnicityVillageRepository).saveAll(ethnicityVillageListCaptor.capture());

    }
    //@Test
    //void testFindByVillageIdAndVillageStatusDateDeleteNotNull() {
    //    Long villageId = 1L;
    //    boolean status = true;
//
    //    List<EthnicityVillage> sampleEthnicityVillages = new ArrayList<>();
    //    sampleEthnicityVillages.add(new EthnicityVillage());
    //    sampleEthnicityVillages.add(new EthnicityVillage());
//
    //    when(ethnicityVillageRepository.findByVillageIdAndVillageStatusAndDateDeleteNotNull(villageId, status)).thenReturn(sampleEthnicityVillages);
//
    //    List<EthnicityVillage> result = ethnicityVillageService.findByVillageIdAndVillageStatusDateDeleteNotNull(villageId, status);
//
    //    verify(ethnicityVillageRepository).findByVillageIdAndVillageStatusAndDateDeleteNotNull(villageId, status);
//
    //    assertEquals(sampleEthnicityVillages, result);
    //}
}
