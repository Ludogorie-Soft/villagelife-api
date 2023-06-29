package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.EthnicityVillageDTO;
import com.example.ludogorieSoft.village.model.Ethnicity;
import com.example.ludogorieSoft.village.model.EthnicityVillage;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.EthnicityRepository;
import com.example.ludogorieSoft.village.repositories.EthnicityVillageRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class EthnicityVillageServiceTest {
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
        EthnicityRepository ethnicityRepository = mock(EthnicityRepository.class);
        VillageRepository villageRepository = mock(VillageRepository.class);
        villageService = mock(VillageService.class);
        ethnicityService = mock(EthnicityService.class);
        modelMapper = mock(ModelMapper.class);
        ethnicityVillageService = new EthnicityVillageService(modelMapper, ethnicityVillageRepository, villageService, ethnicityService);
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

        assertThrows(ApiRequestException.class, () -> {
            ethnicityVillageService.deleteEthnicityVillageById(1L);
        });
    }


}
