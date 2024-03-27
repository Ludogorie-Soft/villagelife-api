package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.enums.Residents;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VillageServiceTests {
    private static final int pageNumber = 0;
    private static final int elementsCount = 6;
    private static final String sort = "asc";
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RegionService regionService;
    @Mock
    private AuthService authService;
    @InjectMocks
    private VillageService villageService;

    @BeforeEach
    void setUp() {
        villageRepository = Mockito.mock(VillageRepository.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        regionService = Mockito.mock(RegionService.class);
        authService = Mockito.mock(AuthService.class);
        villageService = new VillageService(
                villageRepository,
                modelMapper,
                regionService,
                authService
        );
    }

    @Test
    void testGetAllVillages() {

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        List<Village> mockVillages = List.of(village1, village2);

        when(villageRepository.findAll()).thenReturn(mockVillages);

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setName("Village1");

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setName("Village2");

        when(modelMapper.map(village1, VillageDTO.class)).thenReturn(villageDTO1);
        when(modelMapper.map(village2, VillageDTO.class)).thenReturn(villageDTO2);

        List<VillageDTO> result = villageService.getAllVillages();

        Mockito.verify(villageRepository).findAll();

        assertEquals(2, result.size());
        assertEquals("Village1", result.get(0).getName());
        assertEquals("Village2", result.get(1).getName());
    }

    @Test
    void testGetVillageByIdWhenFound() {
        Village village = new Village();
        village.setId(1L);
        village.setName("Village1");
        village.setRegion(new Region(1L, "Region1"));

        when(villageRepository.findById(1L)).thenReturn(Optional.of(village));

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village1");
        villageDTO1.setRegion("Region1");

        when(modelMapper.map(village, VillageDTO.class)).thenReturn(villageDTO1);

        VillageDTO result = villageService.getVillageById(1L);

        verify(villageRepository).findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Village1", result.getName());
        assertEquals("Region1", result.getRegion());
    }


    @Test
    void testCreateVillageWhenVillageExists() {
        String villageName = "Sample Village";
        String regionName = "Sample Region";
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName(villageName);
        villageDTO.setRegion(regionName);

        Village existingVillage = new Village();
        existingVillage.setId(1L);
        existingVillage.setName(villageName);
        existingVillage.setStatus(true);
        existingVillage.setRegion(new Region(1L, regionName));

        VillageDTO savedVillageDTO = new VillageDTO();
        savedVillageDTO.setId(1L);
        savedVillageDTO.setName(villageName);
        savedVillageDTO.setRegion(regionName);

        when(villageRepository.findSingleVillageByNameAndRegionName(villageName, regionName)).thenReturn(existingVillage);
        when(villageRepository.save(existingVillage)).thenReturn(existingVillage);
        when(modelMapper.map(existingVillage, VillageDTO.class)).thenReturn(savedVillageDTO);

        VillageDTO resultDTO = villageService.createVillage(villageDTO);

        verify(villageRepository, times(1)).findSingleVillageByNameAndRegionName(villageName, regionName);
        verify(villageRepository, times(1)).save(any(Village.class));
        verify(modelMapper, times(1)).map(existingVillage, VillageDTO.class);
        verify(regionService, times(0)).findRegionByName(regionName);

        Assertions.assertEquals(savedVillageDTO.getId(), resultDTO.getId());
        Assertions.assertEquals(savedVillageDTO.getName(), resultDTO.getName());
        Assertions.assertNotNull(resultDTO);
    }

    @Test
    void testUpdateVillageStatusThrowsException() {
        VillageDTO villageDTO = new VillageDTO();

        when(villageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class,
                () -> villageService.updateVillageStatus(1L, villageDTO));

        verify(villageRepository).findById(1L);

    }

    @Test
    void testUpdateVillageWithExistingVillageId() {
        Long villageId = 123L;
        Village existingVillage = new Village();
        Region region = new Region(1L, "testRegion");
        RegionDTO regionDTO = new RegionDTO(region.getId(), region.getRegionName());
        existingVillage.setId(villageId);
        existingVillage.setName("Existing Village");


        VillageDTO updatedVillage = new VillageDTO();
        updatedVillage.setName("Updated Village");
        updatedVillage.setRegion(region.getRegionName());

        VillageDTO expectedVillageDTO = new VillageDTO();
        expectedVillageDTO.setId(villageId);
        expectedVillageDTO.setName("Updated Village");
        updatedVillage.setRegion(region.getRegionName());

        when(villageRepository.findById(villageId)).thenReturn(Optional.of(existingVillage));
        when(modelMapper.map(existingVillage, VillageDTO.class)).thenReturn(expectedVillageDTO);
        when(regionService.findRegionByName(region.getRegionName())).thenReturn(regionDTO);
        when(regionService.checkRegion(region.getId())).thenReturn(region);

        VillageDTO result = villageService.updateVillage(villageId, updatedVillage);

        verify(villageRepository, times(1)).findById(villageId);
        verify(villageRepository, times(1)).save(existingVillage);
        assertEquals(expectedVillageDTO, result);
    }

    @Test
    void testUpdateVillageExceptionCase() {

        Long villageId = 1L;

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("Updated Village");

        when(villageRepository.findById(villageId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageService.updateVillage(villageId, villageDTO));
        verify(villageRepository, times(1)).findById(villageId);
        verify(villageRepository, never()).save(any(Village.class));
    }
}

