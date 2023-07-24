package com.example.ludogorieSoft.village.services;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ludogorieSoft.village.dtos.RegionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Region;
import com.example.ludogorieSoft.village.repositories.RegionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {
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

        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testGetRegionByIdReturnsRegionDTO() {
        Region region = new Region();
        region.setId(1L);

        RegionDTO expectedRegionDTO = new RegionDTO();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        when(regionService.regionToRegionDTO(region)).thenReturn(expectedRegionDTO);

        RegionDTO actualRegionDTO = regionService.getRegionById(1L);
        Assertions.assertNotNull(actualRegionDTO);
        Assertions.assertEquals(expectedRegionDTO, actualRegionDTO);
    }

    @Test
    void testGetRegionByIdThrowsExceptionForNonExistingId() {
        when(regionRepository.findById(3L)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> regionService.getRegionById(3L));

        Assertions.assertEquals("Region not found", exception.getMessage());
    }
    @Test
    void testCreateRegionReturnsRegionDTO() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("Test Region");

        when(regionRepository.existsByRegionName(regionDTO.getRegionName())).thenReturn(false);

        RegionDTO createdRegionDTO = regionService.createRegion(regionDTO);
        Assertions.assertNotNull(createdRegionDTO);
        Assertions.assertEquals(regionDTO, createdRegionDTO);

        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    void testCreateRegionThrowsExceptionForBlankRegionName() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("");

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> regionService.createRegion(regionDTO));

        Assertions.assertEquals("Region name is blank", exception.getMessage());

        verify(regionRepository, never()).save(any(Region.class));
    }

    @Test
    void testCreateRegionThrowsExceptionForExistingRegionName() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("Existing Region");

        when(regionRepository.existsByRegionName(regionDTO.getRegionName())).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> regionService.createRegion(regionDTO));

        Assertions.assertEquals("Region with name: Existing Region already exists", exception.getMessage());

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
        Assertions.assertNotNull(updatedRegionDTO);
        Assertions.assertEquals(regionDTO.getRegionName(), updatedRegionDTO.getRegionName());

        verify(regionRepository, times(1)).save(region);
    }

    @Test
    void testUpdateRegionThrowsExceptionForRegionNotFound() {
        when(regionRepository.findById(1L)).thenReturn(Optional.empty());

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("");

        assertThrows(ApiRequestException.class, () -> regionService.updateRegion(1L, regionDTO));

        RegionDTO regionDTOWithoutName = new RegionDTO();

        assertThrows(ApiRequestException.class, () -> regionService.updateRegion(1L, regionDTOWithoutName));

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

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> regionService.updateRegion(1L, regionDTO));
        Assertions.assertEquals("Region: Existing Region already exists", exception.getMessage());

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

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> regionService.deleteRegionById(1L));
        Assertions.assertEquals("Region with id: 1 not found", exception.getMessage());

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

        Assertions.assertNotNull(result);
        Assertions.assertEquals(regionName, result.getRegionName());
    }

    @Test
    void testFindRegionByNameThrowsExceptionForNonexistentRegion() {
        String regionName = "Nonexistent Region";

        when(regionRepository.findByRegionName(regionName)).thenReturn(null);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> regionService.findRegionByName(regionName));

        verify(regionRepository).findByRegionName(regionName);

        Assertions.assertEquals("Region not found", exception.getMessage());
    }
}
