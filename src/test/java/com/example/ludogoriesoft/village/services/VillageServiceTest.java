package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.VillageRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class VillageServiceTest {
    private static final int pageNumber = 0;
    private static final int elementsCount = 6;
    @InjectMocks
    private VillageService villageService;
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RegionService regionService;
    @Mock
    private AuthService authService;
    @Mock
    private TranslatorService translatorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVillageToVillageDTO() {

        VillageDTO expectedVillageDTO = new VillageDTO();
        expectedVillageDTO.setId(1L);

        Village village = new Village();
        village.setId(1L);

        when(modelMapper.map(village, VillageDTO.class)).thenReturn(expectedVillageDTO);

        VillageDTO villageDTO = villageService.villageToVillageDTO(village);

        assertEquals(village.getId(), villageDTO.getId());
    }

    @Test
    void testGetAllVillages() {

        List<Village> villages = new ArrayList<>();
        villages.add(new Village());
        villages.add(new Village());
        villages.add(new Village());

        when(villageRepository.findAll()).thenReturn(villages);

        List<VillageDTO> villageDTOs = villageService.getAllVillages();

        assertEquals(villages.size(), villageDTOs.size());

        verify(villageRepository, times(1)).findAll();
    }

    @Test
    void testGetVillageByIdWhenExist() {
        Long villageId = 1L;

        VillageDTO expectedVillageDTO = new VillageDTO();
        expectedVillageDTO.setId(1L);

        Village village = new Village();
        village.setId(1L);

        when(modelMapper.map(village, VillageDTO.class)).thenReturn(expectedVillageDTO);
        when(villageRepository.findById(villageId)).thenReturn(Optional.of(village));

        VillageDTO villageDTO = villageService.getVillageById(villageId);

        assertEquals(village.getId(), villageDTO.getId());
        verify(villageRepository, times(1)).findById(villageId);
    }

    @Test
    void testGetVillageByIdWhenNotExist() {
        Long villageId = 1L;

        when(villageRepository.findById(villageId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageService.getVillageById(villageId));
        verify(villageRepository, times(1)).findById(villageId);
    }

    @Test
    void testUpdateVillageExceptionCase() {
        Long villageId = 1L;

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("Updated Village");

        when(villageRepository.findById(villageId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageService.updateVillageStatus(villageId, villageDTO));
        verify(villageRepository, times(1)).findById(villageId);
        verify(villageRepository, never()).save(any(Village.class));
    }

    @Test
    void testDeleteVillagePositiveCase() {
        Long villageId = 1L;

        when(villageRepository.existsById(villageId)).thenReturn(true);

        villageService.deleteVillage(villageId);

        verify(villageRepository, times(1)).deleteById(villageId);
    }

    @Test
    void testDeleteVillageExceptionCase() {

        Long villageId = 1L;

        when(villageRepository.existsById(villageId)).thenReturn(false);

        assertThrows(ApiRequestException.class, () -> villageService.deleteVillage(villageId));
    }

    @Test
    void checkVillageShouldReturnExistingVillage() {
        Long villageId = 1L;
        Village existingVillage = new Village();
        existingVillage.setId(villageId);
        Optional<Village> optionalVillage = Optional.of(existingVillage);

        when(villageRepository.findById(villageId)).thenReturn(optionalVillage);

        Village result = villageService.checkVillage(villageId);

        verify(villageRepository, times(1)).findById(villageId);
        assertEquals(existingVillage, result);
    }

    @Test
    void checkVillageShouldThrowExceptionWhenVillageNotFound() {
        Long villageId = 1L;
        Optional<Village> optionalVillage = Optional.empty();

        when(villageRepository.findById(villageId)).thenReturn(optionalVillage);

        Assertions.assertThrows(ApiRequestException.class, () -> villageService.checkVillage(villageId));

        verify(villageRepository, times(1)).findById(villageId);
    }

    @Test
    void testCreateVillageWhitNullValues() {
        Village village = new Village();
        village.setName("null");
        village.setStatus(false);

        Village savedVillage = new Village();
        savedVillage.setId(1L);

        when(villageRepository.save(any(Village.class))).thenAnswer(invocation -> {
            Village createdVillage = invocation.getArgument(0);
            createdVillage.setId(1L);
            return createdVillage;
        });

        Long createdVillageId = villageService.createVillageWhitNullValues();

        assertEquals(1L, createdVillageId);
    }

    @Test
    void testCreateNewVillage() {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("NewVillage");
        villageDTO.setLatinName("NewVillage");
        villageDTO.setRegion("NewRegion");
        villageDTO.setDateUpload(now());
        villageDTO.setStatus(false);

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setRegionName("NewRegion");
        when(regionService.findRegionByName("NewRegion")).thenReturn(regionDTO);
        when(regionService.findRegionByName(anyString())).thenReturn(regionDTO);

        Village village = new Village();
        village.setName("NewVillage");
        village.setLatinName("NewVillage");
        village.setRegion(new Region(1L, "NewRegion"));
        village.setStatus(false);
        Village savedVillage = new Village();
        savedVillage.setId(1L);
        when(villageRepository.findSingleVillageByNameAndRegionName("NewVillage", "NewRegion")).thenReturn(null);
        when(villageRepository.save(any(Village.class))).thenReturn(savedVillage);
        when(modelMapper.map(savedVillage, VillageDTO.class)).thenReturn(villageDTO);
        when(translatorService.translateToLatin("NewVillage")).thenReturn("NewVillage");

        VillageDTO result = villageService.createVillage(villageDTO);

        assertEquals("NewVillage", result.getName());
        assertEquals("NewRegion", result.getRegion());
        assertFalse(result.getStatus());
        assertTrue(result.getDateUpload().isBefore(now().plusSeconds(1)));
        assertTrue(result.getDateUpload().isAfter(now().minusSeconds(1)));
    }

    @Test
    void testUpdateExistingVillageStatus() {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("ExistingVillage");
        villageDTO.setRegion("ExistingRegion");
        villageDTO.setStatus(true);
        villageDTO.setDateUpload(now());

        Village existingVillage = new Village();
        existingVillage.setName("ExistingVillage");
        existingVillage.setRegion(new Region(1L, "ExistingRegion"));
        existingVillage.setStatus(true);
        when(villageRepository.findSingleVillageByNameAndRegionName("ExistingVillage", "ExistingRegion")).thenReturn(existingVillage);
        when(villageRepository.save(any(Village.class))).thenReturn(existingVillage);
        when(modelMapper.map(existingVillage, VillageDTO.class)).thenReturn(villageDTO);

        VillageDTO result = villageService.createVillage(villageDTO);

        assertEquals("ExistingVillage", result.getName());
        assertEquals("ExistingRegion", result.getRegion());
        assertTrue(result.getStatus());
        assertTrue(result.getDateUpload().isBefore(now().plusSeconds(1)));
        assertTrue(result.getDateUpload().isAfter(now().minusSeconds(1)));
    }

    @Test
    void testIncreaseApprovedResponsesCountWhenValidVillageId() {
        Long villageId = 123L;
        Village testVillage = new Village();
        testVillage.setId(villageId);
        testVillage.setApprovedResponsesCount(2);
        Mockito.when(villageRepository.findById(villageId))
                .thenReturn(Optional.of(testVillage));
        villageService.increaseApprovedResponsesCount(villageId);
        Mockito.verify(villageRepository, Mockito.times(1)).save(testVillage);
        assert (testVillage.getApprovedResponsesCount() == 3);
    }

    @Test
    void testIncreaseApprovedResponsesCountWhenInvalidVillageId() {
        Long invalidVillageId = 456L;
        villageService.increaseApprovedResponsesCount(invalidVillageId);
        Mockito.verify(villageRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testGetVillageByNameWhenNotFound() {
        String villageName = "NonExistentVillage";
        when(villageRepository.findByName(villageName)).thenReturn(Collections.emptyList());
        assertThrows(ApiRequestException.class, () -> villageService.getVillageByName(villageName));
    }

    @Test
    void testGetVillageByNameWhenMultipleFound() {
        String villageName = "DuplicateVillage";
        Village village1 = new Village();
        Village village2 = new Village();
        village1.setName(villageName);
        village2.setName(villageName);
        when(villageRepository.findByName(villageName)).thenReturn(List.of(village1, village2));
        assertThrows(ApiRequestException.class, () -> villageService.getVillageByName(villageName));
    }

    @Test
    void testUpdateVillageStatusSuccess() {
        Long villageId = 1L;
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(villageId);
        villageDTO.setName("UpdatedVillage");
        villageDTO.setRegion("UpdatedRegion");
        villageDTO.setStatus(true);

        Village existingVillage = new Village();
        existingVillage.setId(villageId);
        existingVillage.setName("OriginalVillage");
        existingVillage.setRegion(new Region());
        existingVillage.setStatus(false);

        RegionDTO updatedRegionDTO = new RegionDTO();
        updatedRegionDTO.setId(1L);

        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setId(1L);

        when(villageRepository.findById(villageId)).thenReturn(Optional.of(existingVillage));
        when(regionService.findRegionByName(villageDTO.getRegion())).thenReturn(updatedRegionDTO);
        when(authService.getAdministratorInfo()).thenReturn(alternativeUserDTO);
        when(villageRepository.save(any(Village.class))).thenReturn(existingVillage);
        when(modelMapper.map(any(Village.class), eq(VillageDTO.class))).thenReturn(villageDTO);
        when(modelMapper.map(any(AlternativeUser.class), eq(AlternativeUserDTO.class))).thenReturn(new AlternativeUserDTO());

        VillageDTO updatedVillageDTO = villageService.updateVillageStatus(villageId, villageDTO);

        assertNotNull(updatedVillageDTO);
        assertEquals(villageId, updatedVillageDTO.getId());
        assertEquals(villageDTO.getName(), updatedVillageDTO.getName());
        assertTrue(updatedVillageDTO.getStatus());
    }

    @Test
    void testUpdateVillageStatusNotFound() {
        Long villageId = 1L;
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(villageId);

        when(villageRepository.findById(villageId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageService.updateVillageStatus(villageId, villageDTO));
    }

    @Test
    void testGetVillagesByStatus() {
        boolean status = true;

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("TestVillage1");
        village1.setStatus(status);

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("TestVillage1");
        villageDTO.setRegion("NewRegion");
        villageDTO.setDateUpload(now());
        villageDTO.setStatus(false);

        List<Village> villageList = List.of(village1);
        Page<Village> page = new PageImpl<>(villageList, PageRequest.of(pageNumber, elementsCount), villageList.size());

        when(villageRepository.findByStatus(status, PageRequest.of(pageNumber, elementsCount)))
                .thenReturn(page);
        when(modelMapper.map(any(Village.class), eq(VillageDTO.class))).thenReturn(villageDTO);


        Page<VillageDTO> resultPage = villageService.getVillagesByStatus(status, PageRequest.of(pageNumber, elementsCount));

        assertEquals(1, resultPage.getTotalElements());
        assertEquals(1, resultPage.getContent().size());
        assertEquals("TestVillage1", resultPage.getContent().get(0).getName());
    }
    @Test
    void testGetVillageByNameAndRegionName_WhenVillageExists() {
       Region region = new Region(1L,"RegionName");
        String key = "VillageName, RegionName";

        Village mockVillage = new Village();
        mockVillage.setId(1L);
        mockVillage.setName("VillageName");
        mockVillage.setRegion(region);

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(1L);
        villageDTO.setName("VillageName");
        villageDTO.setRegion("RegionName");

        when(villageRepository.findSingleVillageByNameAndRegionName_forUpload("VillageName", "RegionName"))
                .thenReturn(List.of(mockVillage));
        when(modelMapper.map(any(Village.class), eq(VillageDTO.class))).thenReturn(villageDTO);

        VillageDTO result = villageService.getVillageByNameAndRegionName(key);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("VillageName", result.getName());
        assertEquals("RegionName", result.getRegion());
    }

    @Test
    void testGetVillageByNameAndRegionName_WhenVillageDoesNotExist() {
        String key = "NonExistingVillage, NonExistingRegion";

        when(villageRepository.findSingleVillageByNameAndRegionName_forUpload("NonExistingVillage", "NonExistingRegion"))
                .thenReturn(new ArrayList<>());

        VillageDTO result = villageService.getVillageByNameAndRegionName(key);

        assertNull(result);
    }
}
