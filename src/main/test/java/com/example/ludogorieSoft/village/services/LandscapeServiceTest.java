package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.LandscapeDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Landscape;
import com.example.ludogorieSoft.village.repositories.LandscapeRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LandscapeServiceTest {

    @Mock
    private LandscapeRepository landscapeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private LandscapeService landscapeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLandscapesWithExistingLandscapes() {
        Landscape landscape1 = new Landscape(1L, "Landscape 1");
        Landscape landscape2 = new Landscape(2L, "Landscape 2");

        LandscapeDTO landscapeDTO1 = new LandscapeDTO(1L, "Landscape 1");
        LandscapeDTO landscapeDTO2 = new LandscapeDTO(2L, "Landscape 2");

        List<Landscape> landscapes = new ArrayList<>();
        landscapes.add(landscape1);
        landscapes.add(landscape2);

        List<LandscapeDTO> expectedLandscapes = new ArrayList<>();
        expectedLandscapes.add(landscapeDTO1);
        expectedLandscapes.add(landscapeDTO2);

        when(landscapeRepository.findAll()).thenReturn(landscapes);
        when(modelMapper.map(landscape1, LandscapeDTO.class)).thenReturn(landscapeDTO1);
        when(modelMapper.map(landscape2, LandscapeDTO.class)).thenReturn(landscapeDTO2);

        List<LandscapeDTO> result = landscapeService.getAllLandscapes();

        verify(landscapeRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(landscape1, LandscapeDTO.class);
        verify(modelMapper, times(1)).map(landscape2, LandscapeDTO.class);
        Assertions.assertEquals(expectedLandscapes, result);
    }

    @Test
    void testGetAllLandscapesWithNoLandscapes() {
        List<Landscape> landscapes = new ArrayList<>();
        List<LandscapeDTO> expectedLandscapes = new ArrayList<>();

        when(landscapeRepository.findAll()).thenReturn(landscapes);
        List<LandscapeDTO> result = landscapeService.getAllLandscapes();

        verify(landscapeRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(Landscape.class), eq(LandscapeDTO.class));
        Assertions.assertEquals(expectedLandscapes, result);
    }

    @Test
    void testGetLandscapeByIdWithExistingLandscapeId() {
        Long landscapeId = 123L;
        Landscape existingLandscape = new Landscape(landscapeId, "Existing Landscape");

        LandscapeDTO expectedLandscapeDTO = new LandscapeDTO(landscapeId, "Existing Landscape");
        when(landscapeRepository.findById(landscapeId)).thenReturn(Optional.of(existingLandscape));
        when(modelMapper.map(existingLandscape, LandscapeDTO.class)).thenReturn(expectedLandscapeDTO);

        LandscapeDTO result = landscapeService.getLandscapeById(landscapeId);

        verify(landscapeRepository, times(1)).findById(landscapeId);
        verify(modelMapper, times(1)).map(existingLandscape, LandscapeDTO.class);
        Assertions.assertEquals(expectedLandscapeDTO, result);
    }

    @Test
    void testGetLandscapeByIdWithNonExistingLandscapeIdThenThrowsApiRequestException() {
        Long landscapeId = 123L;
        when(landscapeRepository.findById(landscapeId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class, () -> landscapeService.getLandscapeById(landscapeId));
        verify(landscapeRepository, times(1)).findById(landscapeId);
        verify(modelMapper, never()).map(any(Landscape.class), eq(LandscapeDTO.class));
    }

    @Test
    void testCreateLandscapeWithNonExistingLandscapeName() {
        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setLandscapeName("New Landscape");

        Landscape landscape = new Landscape();
        landscape.setLandscapeName("New Landscape");

        when(landscapeRepository.existsByLandscapeName(landscapeDTO.getLandscapeName())).thenReturn(false);
        when(landscapeRepository.save(any(Landscape.class))).thenReturn(landscape);

        LandscapeDTO result = landscapeService.createLandscape(landscapeDTO);

        verify(landscapeRepository, times(1)).existsByLandscapeName(landscapeDTO.getLandscapeName());
        verify(landscapeRepository, times(1)).save(any(Landscape.class));
        Assertions.assertEquals(landscapeDTO, result);
    }

    @Test
    void testCreateLandscapeWithExistingLandscapeNameThenThrowsApiRequestException() {
        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setLandscapeName("Existing Landscape");

        when(landscapeRepository.existsByLandscapeName(landscapeDTO.getLandscapeName())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class, () -> landscapeService.createLandscape(landscapeDTO));
        verify(landscapeRepository, times(1)).existsByLandscapeName(landscapeDTO.getLandscapeName());
        verify(landscapeRepository, never()).save(any(Landscape.class));
    }


    @Test
    void testCreateLandscapeExistingNameThrowsApiRequestException() {
        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setLandscapeName("Mountain");

        when(landscapeRepository.existsByLandscapeName(landscapeDTO.getLandscapeName())).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> landscapeService.createLandscape(landscapeDTO));
        verify(landscapeRepository, never()).save(any(Landscape.class));
    }


    @Test
    void testUpdateLandscapeNonExistingIdThrowsApiRequestException() {
        Long id = 1L;
        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setLandscapeName("Forest");

        when(landscapeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> landscapeService.updateLandscape(id, landscapeDTO));
        verify(landscapeRepository, never()).save(any(Landscape.class));
    }

    @Test
    void testUpdateLandscapeExistingNameThrowsApiRequestException() {
        // Arrange
        Long id = 1L;
        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setLandscapeName("Mountain");

        Landscape existingLandscape = new Landscape();
        existingLandscape.setId(id);
        existingLandscape.setLandscapeName("Forest");

        when(landscapeRepository.findById(id)).thenReturn(Optional.of(existingLandscape));
        when(landscapeRepository.existsByLandscapeName(landscapeDTO.getLandscapeName())).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> landscapeService.updateLandscape(id, landscapeDTO));
        verify(landscapeRepository, never()).save(any(Landscape.class));
    }

    @Test
    void testDeleteLandscapeWithExistingId() {
        Long landscapeId = 123L;
        Landscape landscape = new Landscape();
        when(landscapeRepository.findById(landscapeId)).thenReturn(Optional.of(landscape));
        landscapeService.deleteLandscape(landscapeId);
        verify(landscapeRepository, times(1)).findById(landscapeId);
        verify(landscapeRepository, times(1)).delete(landscape);
    }

    @Test
    void testDeleteLandscapeWithNonExistingIdThenThrowsApiRequestException() {
        Long landscapeId = 123L;
        when(landscapeRepository.findById(landscapeId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class, () -> landscapeService.deleteLandscape(landscapeId));
        verify(landscapeRepository, times(1)).findById(landscapeId);
        verify(landscapeRepository, never()).delete(any(Landscape.class));
    }
}
