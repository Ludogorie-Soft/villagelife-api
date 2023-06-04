package com.example.ludogorieSoft.village.services_tests;

import com.example.ludogorieSoft.village.dtos.EthnicityDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Ethnicity;
import com.example.ludogorieSoft.village.repositories.EthnicityRepository;
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

public class EthnicityServiceTest {
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private EthnicityRepository ethnicityRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EthnicityService ethnicityService;

    @Test
    public void testGetAllEthnicities() {
        Ethnicity ethnicity1 = new Ethnicity(1L, "Ethnicity 1");
        Ethnicity ethnicity2 = new Ethnicity(2L,"Ethnicity 2");

        List<Ethnicity> ethnicities = new ArrayList<>();
        ethnicities.add(ethnicity1);
        ethnicities.add(ethnicity2);

        EthnicityDTO ethnicityDTO1 = new EthnicityDTO(1L, "Ethnicity 1");
        EthnicityDTO ethnicityDTO2 = new EthnicityDTO(2L, "Ethnicity 2");

        List<EthnicityDTO> expectedEthnicityDTOs = new ArrayList<>();
        expectedEthnicityDTOs.add(ethnicityDTO1);
        expectedEthnicityDTOs.add(ethnicityDTO2);

        when(ethnicityRepository.findAll()).thenReturn(ethnicities);
        when(modelMapper.map(ethnicity1, EthnicityDTO.class)).thenReturn(ethnicityDTO1);
        when(modelMapper.map(ethnicity2, EthnicityDTO.class)).thenReturn(ethnicityDTO2);

        List<EthnicityDTO> result = ethnicityService.getAllEthnicities();

        verify(ethnicityRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(ethnicity1, EthnicityDTO.class);
        verify(modelMapper, times(1)).map(ethnicity2, EthnicityDTO.class);
        Assertions.assertEquals(expectedEthnicityDTOs, result);
    }

    @Test
    public void testGetAllEthnicitiesWithNoEthnicities() {
        List<Ethnicity> ethnicities = new ArrayList<>();
        List<EthnicityDTO> expectedEthnicityDTOs = new ArrayList<>();

        when(ethnicityRepository.findAll()).thenReturn(ethnicities);

        List<EthnicityDTO> result = ethnicityService.getAllEthnicities();

        verify(ethnicityRepository, times(1)).findAll();
        Assertions.assertEquals(expectedEthnicityDTOs, result);
    }
    @Test
    public void testGetEthnicityByIdWithExistingEthnicityIdThenReturnsEthnicityDTO() {
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
    public void testGetEthnicityByIdWithNonExistingEthnicityIdThenThrowsApiRequestException() {
        Long ethnicityId = 123L;

        when(ethnicityRepository.findById(ethnicityId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> ethnicityService.getEthnicityById(ethnicityId));
        verify(ethnicityRepository, times(1)).findById(ethnicityId);
        verify(modelMapper, never()).map(any(), eq(EthnicityDTO.class));
    }

    @Test
    public void testCreateEthnicity() {
        Ethnicity ethnicity = new Ethnicity(1L, "Test Ethnicity");

        EthnicityDTO expectedEthnicityDTO = new EthnicityDTO(1L, "Test Ethnicity");

        when(ethnicityRepository.save(ethnicity)).thenReturn(ethnicity);
        when(modelMapper.map(ethnicity, EthnicityDTO.class)).thenReturn(expectedEthnicityDTO);

        EthnicityDTO result = ethnicityService.createEthnicity(ethnicity);

        verify(ethnicityRepository, times(1)).save(ethnicity);
        Assertions.assertEquals(expectedEthnicityDTO, result);
    }
    @Test
    public void testDeleteEthnicityByIdWithExistingEthnicityId() {
        Long ethnicityId = 123L;
        when(ethnicityRepository.existsById(ethnicityId)).thenReturn(true);
        ethnicityService.deleteEthnicityById(ethnicityId);
        verify(ethnicityRepository, times(1)).existsById(ethnicityId);
        verify(ethnicityRepository, times(1)).deleteById(ethnicityId);
    }

    @Test
    public void testDeleteEthnicityByIdWithNonExistingEthnicityIdThenThrowsApiRequestException() {
        Long ethnicityId = 123L;
        when(ethnicityRepository.existsById(ethnicityId)).thenReturn(false);
        Assertions.assertThrows(ApiRequestException.class, () -> ethnicityService.deleteEthnicityById(ethnicityId));
        verify(ethnicityRepository, times(1)).existsById(ethnicityId);
        verify(ethnicityRepository, never()).deleteById(ethnicityId);
    }
    @Test
    public void testUpdateEthnicityWithExistingEthnicityId() {
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
    public void testUpdateEthnicityWithNonExistingEthnicityIdThenThrowsApiRequestException() {
        Long ethnicityId = 123L;
        Ethnicity updatedEthnicity = new Ethnicity();
        updatedEthnicity.setEthnicityName("Updated Ethnicity");
        when(ethnicityRepository.findById(ethnicityId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class, () -> ethnicityService.updateEthnicity(ethnicityId, updatedEthnicity));
        verify(ethnicityRepository, times(1)).findById(ethnicityId);
        verify(ethnicityRepository, never()).save(any(Ethnicity.class));
    }
}
