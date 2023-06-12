package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.EthnicityDTO;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import com.example.ludogoriesoft.village.model.Ethnicity;
import com.example.ludogoriesoft.village.repositories.EthnicityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertThrows;

class EthnicityServiceTest {
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private EthnicityRepository ethnicityRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EthnicityService ethnicityService;

    @Test
    void testGetAllEthnicities() {
        Ethnicity ethnicity1 = new Ethnicity(1L, "Ethnicity 1");
        Ethnicity ethnicity2 = new Ethnicity(2L, "Ethnicity 2");

        List<Ethnicity> ethnicities = new ArrayList<>();
        ethnicities.add(ethnicity1);
        ethnicities.add(ethnicity2);

        EthnicityDTO ethnicityDTO1 = new EthnicityDTO(1L, "Ethnicity 1");
        EthnicityDTO ethnicityDTO2 = new EthnicityDTO(2L, "Ethnicity 2");

        List<EthnicityDTO> expectedEthnicityDTOs = new ArrayList<>();
        expectedEthnicityDTOs.add(ethnicityDTO1);
        expectedEthnicityDTOs.add(ethnicityDTO2);

        when(ethnicityRepository.findAllByOrderByIdAsc()).thenReturn(ethnicities);
        when(modelMapper.map(ethnicity1, EthnicityDTO.class)).thenReturn(ethnicityDTO1);
        when(modelMapper.map(ethnicity2, EthnicityDTO.class)).thenReturn(ethnicityDTO2);

        List<EthnicityDTO> result = ethnicityService.getAllEthnicities();

        verify(ethnicityRepository, times(1)).findAllByOrderByIdAsc();
        verify(modelMapper, times(1)).map(ethnicity1, EthnicityDTO.class);
        verify(modelMapper, times(1)).map(ethnicity2, EthnicityDTO.class);
        Assertions.assertEquals(expectedEthnicityDTOs, result);
    }


    @Test
    void testGetAllEthnicitiesWithNoEthnicities() {
        List<Ethnicity> ethnicities = new ArrayList<>();
        List<EthnicityDTO> expectedEthnicityDTOs = new ArrayList<>();

        when(ethnicityRepository.findAllByOrderByIdAsc()).thenReturn(ethnicities);

        List<EthnicityDTO> result = ethnicityService.getAllEthnicities();

        verify(ethnicityRepository, times(1)).findAllByOrderByIdAsc();
        Assertions.assertEquals(expectedEthnicityDTOs, result);
    }

    @Test
    void testGetEthnicityByIdWithExistingEthnicityIdThenReturnsEthnicityDTO() {
        Long ethnicityId = 123L;
        Ethnicity existingEthnicity = new Ethnicity(ethnicityId, "Test Ethnicity");

        EthnicityDTO expectedEthnicityDTO = new EthnicityDTO(ethnicityId, "Test Ethnicity");

        when(ethnicityRepository.findById(ethnicityId)).thenReturn(Optional.of(existingEthnicity));
        when(modelMapper.map(existingEthnicity, EthnicityDTO.class)).thenReturn(expectedEthnicityDTO);

        EthnicityDTO result = ethnicityService.getEthnicityById(ethnicityId);

        verify(ethnicityRepository, times(1)).findById(ethnicityId);
        verify(modelMapper, times(1)).map(existingEthnicity, EthnicityDTO.class);
        Assertions.assertEquals(expectedEthnicityDTO, result);
    }

    @Test
    void testGetEthnicityByIdWithNonExistingEthnicityIdThenThrowsApiRequestException() {
        Long ethnicityId = 123L;
        when(ethnicityRepository.findById(ethnicityId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> ethnicityService.getEthnicityById(ethnicityId));
        verify(ethnicityRepository, times(1)).findById(ethnicityId);
        verify(modelMapper, never()).map(any(), eq(EthnicityDTO.class));
    }

    @Test
    void testCreateEthnicityWhenEthnicityDoesNotExist() {
        EthnicityDTO ethnicityDTO = new EthnicityDTO();
        ethnicityDTO.setEthnicityName("Ethnicity 1");

        Ethnicity ethnicity = new Ethnicity();
        ethnicity.setEthnicityName("Ethnicity 1");

        EthnicityDTO expectedDTO = new EthnicityDTO();
        expectedDTO.setEthnicityName("Ethnicity 1");

        when(ethnicityRepository.existsByEthnicityName(ethnicityDTO.getEthnicityName())).thenReturn(false);
        when(modelMapper.map(ethnicityDTO, Ethnicity.class)).thenReturn(ethnicity);
        when(ethnicityRepository.save(ethnicity)).thenReturn(ethnicity);
        when(modelMapper.map(ethnicity, EthnicityDTO.class)).thenReturn(expectedDTO);

        EthnicityDTO resultDTO = ethnicityService.createEthnicity(ethnicityDTO);

        Assertions.assertEquals(expectedDTO.getEthnicityName(), resultDTO.getEthnicityName());
    }

    @Test
    void testCreateEthnicityWhenEthnicityExists() {
        EthnicityDTO ethnicityDTO = new EthnicityDTO();
        ethnicityDTO.setEthnicityName("Ethnicity 1");

        when(ethnicityRepository.existsByEthnicityName(ethnicityDTO.getEthnicityName())).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> ethnicityService.createEthnicity(ethnicityDTO));
    }


    @Test
    void testUpdateEthnicityWithExistingEthnicityId() {
        Long ethnicityId = 123L;
        Ethnicity existingEthnicity = new Ethnicity(ethnicityId, "Existing Ethnicity");

        Ethnicity updatedEthnicity = new Ethnicity();
        updatedEthnicity.setEthnicityName("Updated Ethnicity");

        EthnicityDTO expectedEthnicityDTO = new EthnicityDTO(ethnicityId, "Updated Ethnicity");

        when(ethnicityRepository.findById(ethnicityId)).thenReturn(Optional.of(existingEthnicity));
        when(modelMapper.map(existingEthnicity, EthnicityDTO.class)).thenReturn(expectedEthnicityDTO);

        EthnicityDTO result = ethnicityService.updateEthnicity(ethnicityId, updatedEthnicity);

        verify(ethnicityRepository, times(1)).findById(ethnicityId);
        verify(ethnicityRepository, times(1)).save(existingEthnicity);
        Assertions.assertEquals(expectedEthnicityDTO, result);
    }

    @Test
    void testUpdateEthnicityWithNonExistingEthnicityIdThenThrowsApiRequestException() {
        Long ethnicityId = 123L;
        Ethnicity updatedEthnicity = new Ethnicity();
        updatedEthnicity.setEthnicityName("Updated Ethnicity");
        when(ethnicityRepository.findById(ethnicityId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class, () -> ethnicityService.updateEthnicity(ethnicityId, updatedEthnicity));
        verify(ethnicityRepository, times(1)).findById(ethnicityId);
        verify(ethnicityRepository, never()).save(any(Ethnicity.class));
    }

    @Test
    void testDeleteEthnicityByIdWithExistingEthnicityId() {
        Long ethnicityId = 123L;
        when(ethnicityRepository.existsById(ethnicityId)).thenReturn(true);
        ethnicityService.deleteEthnicityById(ethnicityId);
        verify(ethnicityRepository, times(1)).existsById(ethnicityId);
        verify(ethnicityRepository, times(1)).deleteById(ethnicityId);
    }


    @Test
    void testDeleteEthnicityByIdWithNonExistingEthnicityIdThenThrowsApiRequestException() {
        Long ethnicityId = 123L;
        when(ethnicityRepository.existsById(ethnicityId)).thenReturn(false);
        Assertions.assertThrows(ApiRequestException.class, () -> ethnicityService.deleteEthnicityById(ethnicityId));
        verify(ethnicityRepository, times(1)).existsById(ethnicityId);
        verify(ethnicityRepository, never()).deleteById(ethnicityId);
    }

    @Test
    void testCheckEthnicityWhenEthnicityExists() {
        Long id = 1L;
        Ethnicity ethnicity = new Ethnicity();
        Mockito.when(ethnicityRepository.findById(id)).thenReturn(Optional.of(ethnicity));

        Ethnicity result = ethnicityService.checkEthnicity(id);

        Assertions.assertEquals(ethnicity, result);
        Mockito.verify(ethnicityRepository).findById(id);
    }

    @Test
    void testCheckEthnicityWhenEthnicityDoesNotExist() {
        Long id = 1L;
        Mockito.when(ethnicityRepository.findById(id)).thenReturn(Optional.empty());

        ApiRequestException exception = Assertions.assertThrows(ApiRequestException.class,
                () -> ethnicityService.checkEthnicity(id));
        Assertions.assertEquals("Ethnicity not found", exception.getMessage());
        Mockito.verify(ethnicityRepository).findById(id);
    }

    @Test
    void testEthnicityToEthnicityDTO() {
        Ethnicity ethnicity = new Ethnicity();
        ethnicity.setId(1L);

        EthnicityDTO expectedDTO = new EthnicityDTO();
        expectedDTO.setId(1L);

        when(modelMapper.map(ethnicity, EthnicityDTO.class)).thenReturn(expectedDTO);

        EthnicityDTO resultDTO = ethnicityService.ethnicityToEthnicityDTO(ethnicity);

        assertEquals(expectedDTO.getId(), resultDTO.getId());
    }

}
