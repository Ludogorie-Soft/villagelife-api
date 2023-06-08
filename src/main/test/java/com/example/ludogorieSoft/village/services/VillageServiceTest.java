package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
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

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


class VillageServiceTest {
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @InjectMocks
    private VillageService villageService;
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private ModelMapper modelMapper;


    @Test
    void testVillageToVillageDTO() {

        VillageDTO expectedVillageDTO = new VillageDTO();
        expectedVillageDTO.setId(1L);

        Village village = new Village();
        village.setId(1L);

        when(modelMapper.map(village, VillageDTO.class)).thenReturn(expectedVillageDTO);

        VillageDTO villageDTO = villageService.villageToVillageDTO(village);

        Assertions.assertEquals(village.getId(), villageDTO.getId());
    }

    @Test
    void testGetAllVillages() {

        List<Village> villages = new ArrayList<>();
        villages.add(new Village());
        villages.add(new Village());
        villages.add(new Village());

        when(villageRepository.findAll()).thenReturn(villages);

        List<VillageDTO> villageDTOs = villageService.getAllVillages();

        Assertions.assertEquals(villages.size(), villageDTOs.size());

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

        Assertions.assertEquals(village.getId(), villageDTO.getId());
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
    void testCreateVillage() {

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("Test Village");

        Village village = new Village();
        village.setId(1L);
        village.setName("Test Village");

        when(modelMapper.map(villageDTO, Village.class)).thenReturn(village);
        when(modelMapper.map(village, VillageDTO.class)).thenReturn(villageDTO);

        VillageDTO createdVillageDTO = villageService.createVillage(villageDTO);

        Assertions.assertEquals(villageDTO.getId(), createdVillageDTO.getId());
        Assertions.assertEquals(villageDTO.getName(), createdVillageDTO.getName());

        verify(villageRepository, times(1)).save(village);
    }

    @Test
    void testUpdateVillageWithExistingVillageId() {
        Long villageId = 123L;
        Village existingVillage = new Village();
        existingVillage.setId(villageId);
        existingVillage.setName("Existing Village");

        VillageDTO updatedVillage = new VillageDTO();
        updatedVillage.setName("Updated Village");

        VillageDTO expectedVillageDTO = new VillageDTO();
        expectedVillageDTO.setId(villageId);
        expectedVillageDTO.setName("Updated Village");

        when(villageRepository.findById(villageId)).thenReturn(Optional.of(existingVillage));
        when(modelMapper.map(existingVillage, VillageDTO.class)).thenReturn(expectedVillageDTO);

        VillageDTO result = villageService.updateVillage(villageId, updatedVillage);

        verify(villageRepository, times(1)).findById(villageId);
        verify(villageRepository, times(1)).save(existingVillage);
        Assertions.assertEquals(expectedVillageDTO, result);
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
        Assertions.assertEquals(existingVillage, result);
    }

    @Test
    void checkVillageShouldThrowExceptionWhenVillageNotFound() {
        Long villageId = 1L;
        Optional<Village> optionalVillage = Optional.empty();

        when(villageRepository.findById(villageId)).thenReturn(optionalVillage);

        Assertions.assertThrows(ApiRequestException.class, () -> villageService.checkVillage(villageId));

        verify(villageRepository, times(1)).findById(villageId);
    }

}