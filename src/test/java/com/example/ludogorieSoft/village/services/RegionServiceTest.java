package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.RegionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Region;
import com.example.ludogorieSoft.village.repositories.RegionRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class RegionServiceTest {
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Mock
    private RegionRepository regionRepository;
    @Mock
    ModelMapper modelMapper;


    @InjectMocks
    private RegionService regionService;

    @Test
    void testCheckRegionValidIdReturnsRegion() {
        Long validId = 1L;
        Region expectedRegion = new Region(validId, "Region Name");
        when(regionRepository.findById(validId)).thenReturn(Optional.of(expectedRegion));

        Region result = regionService.checkRegion(validId);

        Assertions.assertEquals(expectedRegion, result);
    }

    @Test
    void testCheckRegionInvalidIdThrowsApiRequestException() {
        Long invalidId = 100L;
        when(regionRepository.findById(invalidId)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> regionService.checkRegion(invalidId));

        Assertions.assertEquals("Region not found", exception.getMessage());
    }
    @Test
    void testGetAllRegionsReturnsEmptyList() {
        when(regionRepository.findAllByOrderByIdAsc()).thenReturn(new ArrayList<>());

        List<RegionDTO> result = regionService.getAllRegions();

        assertEquals(0, result.size());
    }

    @Test
    void testGetRegionByIdReturnsRegionDTO() {
        Region region = new Region();
        region.setId(1L);

        RegionDTO expectedRegionDTO = new RegionDTO();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        when(regionService.regionToRegionDTO(region)).thenReturn(expectedRegionDTO);

        RegionDTO actualRegionDTO = regionService.getRegionById(1L);
        assertNotNull(actualRegionDTO);
        assertEquals(expectedRegionDTO, actualRegionDTO);
    }

    @Test
    void testGetRegionByIdThrowsExceptionForNonExistingId() {
        when(regionRepository.findById(3L)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            regionService.getRegionById(3L);
        });

        assertEquals("Region not found", exception.getMessage());
    }
    @Test
    void testCreateRegionReturnsRegionDTO() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("Test Region");

        when(regionRepository.existsByRegionName(regionDTO.getRegionName())).thenReturn(false);

        RegionDTO createdRegionDTO = regionService.createRegion(regionDTO);
        assertNotNull(createdRegionDTO);
        assertEquals(regionDTO, createdRegionDTO);

        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    void testCreateRegionThrowsExceptionForBlankRegionName() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("");

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            regionService.createRegion(regionDTO);
        });

        assertEquals("Region name is blank", exception.getMessage());

        verify(regionRepository, never()).save(any(Region.class));
    }

    @Test
    void testCreateRegionThrowsExceptionForExistingRegionName() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("Existing Region");

        when(regionRepository.existsByRegionName(regionDTO.getRegionName())).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            regionService.createRegion(regionDTO);
        });

        assertEquals("Region with name: Existing Region already exists", exception.getMessage());

        verify(regionRepository, never()).save(any(Region.class));
    }

    @Test
    void testUpdateRegionReturnsUpdatedRegionDTO() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("Updated Region");

        Region region = new Region();
        region.setId(1L);
        region.setRegionName("Old Region");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        when(regionService.regionToRegionDTO(region)).thenReturn(regionDTO);
        when(regionRepository.existsByRegionName(regionDTO.getRegionName())).thenReturn(false);

        RegionDTO updatedRegionDTO = regionService.updateRegion(1L, regionDTO);
        assertNotNull(updatedRegionDTO);
        assertEquals(regionDTO.getRegionName(), updatedRegionDTO.getRegionName());

        verify(regionRepository, times(1)).save(region);
    }

    @Test
    void testUpdateRegionThrowsExceptionForRegionNotFound() {
        ApiRequestException exception1 = assertThrows(ApiRequestException.class, () -> {
            regionService.updateRegion(1L, new RegionDTO());
        });
        assertEquals("Region not found", exception1.getMessage());

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("");

        ApiRequestException exception2 = assertThrows(ApiRequestException.class, () -> {
            regionService.updateRegion(1L, regionDTO);
        });
        assertEquals("Region not found", exception2.getMessage());

        RegionDTO regionDTOWithoutName = new RegionDTO();

        ApiRequestException exception3 = assertThrows(ApiRequestException.class, () -> {
            regionService.updateRegion(1L, regionDTOWithoutName);
        });
        assertEquals("Region not found", exception3.getMessage());

        verify(regionRepository, never()).save(any(Region.class));
    }
    @Test
    void testUpdateRegionThrowsExceptionForExistingRegionName() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("Existing Region");

        Region region = new Region();
        region.setId(1L);
        region.setRegionName("Old Region");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        when(regionRepository.existsByRegionName(regionDTO.getRegionName())).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            regionService.updateRegion(1L, regionDTO);
        });
        assertEquals("Region: Existing Region already exists", exception.getMessage());

        verify(regionRepository, never()).save(any(Region.class));
    }
    @Test
    void testDeleteRegionByIdDeletesExistingRegion() {
        Region region = new Region();
        region.setId(1L);

        when(regionRepository.existsById(1L)).thenReturn(true);

        regionService.deleteRegionById(1L);

        verify(regionRepository).deleteById(1L);
    }

    @Test
    void testDeleteRegionByIdThrowsExceptionForNonExistingRegion() {
        when(regionRepository.existsById(1L)).thenReturn(false);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            regionService.deleteRegionById(1L);
        });
        assertEquals("Region with id: 1 not found", exception.getMessage());

        verify(regionRepository, never()).deleteById(anyLong());
    }
    @Test
    void testFindRegionByNameReturnsRegionDTO() {
        String regionName = "Test Region";

        Region region = new Region();
        region.setId(1L);
        region.setRegionName(regionName);
        RegionDTO regionDTO = new RegionDTO(region.getId(), regionName);

        when(regionRepository.findByRegionName(regionName)).thenReturn(region);
        when(regionService.regionToRegionDTO(region)).thenReturn(regionDTO);
        RegionDTO result = regionService.findRegionByName(regionName);

        verify(regionRepository).findByRegionName(regionName);

        assertNotNull(result);
        assertEquals(regionName, result.getRegionName());
    }

    @Test
    void testFindRegionByNameThrowsExceptionForNonexistentRegion() {
        String regionName = "Nonexistent Region";

        when(regionRepository.findByRegionName(regionName)).thenReturn(null);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            regionService.findRegionByName(regionName);
        });

        verify(regionRepository).findByRegionName(regionName);

        assertEquals("Region not found", exception.getMessage());
    }


}
