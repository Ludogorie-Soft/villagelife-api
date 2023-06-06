package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.GroundCategory;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import com.example.ludogorieSoft.village.repositories.GroundCategoryRepository;
import com.example.ludogorieSoft.village.repositories.VillageGroundCategoryRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VillageGroundCategoryServiceTest {
    @Mock
    private VillageGroundCategoryRepository villageGroundCategoryRepository;
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private GroundCategoryRepository groundCategoryRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @Mock
    private GroundCategoryService groundCategoryService;
    @InjectMocks
    private VillageGroundCategoryService villageGroundCategoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDTO() {
        VillageGroundCategory villageGroundCategory = new VillageGroundCategory();
        villageGroundCategory.setId(1L);

        VillageGroundCategoryDTO expectedDTO = new VillageGroundCategoryDTO();
        expectedDTO.setId(1L);

        when(modelMapper.map(villageGroundCategory, VillageGroundCategoryDTO.class)).thenReturn(expectedDTO);

        VillageGroundCategoryDTO resultDTO = villageGroundCategoryService.toDTO(villageGroundCategory);

        assertEquals(expectedDTO.getId(), resultDTO.getId());
    }

    @Test
    void testGetAllVillageGroundCategories() {
        VillageGroundCategory villageGroundCategory1 = new VillageGroundCategory();
        villageGroundCategory1.setId(1L);
        VillageGroundCategory villageGroundCategory2 = new VillageGroundCategory();
        villageGroundCategory2.setId(2L);

        List<VillageGroundCategory> villageGroundCategories = new ArrayList<>();
        villageGroundCategories.add(villageGroundCategory1);
        villageGroundCategories.add(villageGroundCategory2);

        VillageGroundCategoryDTO dto1 = new VillageGroundCategoryDTO();
        dto1.setId(1L);
        VillageGroundCategoryDTO dto2 = new VillageGroundCategoryDTO();
        dto2.setId(2L);

        when(villageGroundCategoryRepository.findAll()).thenReturn(villageGroundCategories);
        when(modelMapper.map(villageGroundCategory1, VillageGroundCategoryDTO.class)).thenReturn(dto1);
        when(modelMapper.map(villageGroundCategory2, VillageGroundCategoryDTO.class)).thenReturn(dto2);

        List<VillageGroundCategoryDTO> resultDTOs = villageGroundCategoryService.getAllVillageGroundCategories();

        assertEquals(2, resultDTOs.size());
        assertEquals(1L, resultDTOs.get(0).getId());
        assertEquals(2L, resultDTOs.get(1).getId());
    }

    @Test
    void testGetByIDWhenIdExists() {
        Long id = 1L;
        VillageGroundCategory villageGroundCategory = new VillageGroundCategory();
        villageGroundCategory.setId(id);
        VillageGroundCategoryDTO expectedDTO = new VillageGroundCategoryDTO();
        expectedDTO.setId(id);

        when(villageGroundCategoryRepository.findById(id)).thenReturn(Optional.of(villageGroundCategory));
        when(modelMapper.map(villageGroundCategory, VillageGroundCategoryDTO.class)).thenReturn(expectedDTO);

        VillageGroundCategoryDTO resultDTO = villageGroundCategoryService.getByID(id);

        assertEquals(expectedDTO.getId(), resultDTO.getId());
    }

    @Test
    void testGetByIDWhenIdDoesNotExist() {
        Long id = 1L;
        when(villageGroundCategoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageGroundCategoryService.getByID(id));
    }


    @Test
    void testUpdateVillageGroundCategory() {
        Long id = 1L;
        Village village = new Village();
        village.setId(1L);
        when(villageService.checkVillage(1L)).thenReturn(village);

        GroundCategory groundCategory = new GroundCategory();
        groundCategory.setId(1L);
        when(groundCategoryService.checkGroundCategory(1L)).thenReturn(groundCategory);

        VillageGroundCategoryDTO inputDTO = new VillageGroundCategoryDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setGroundCategoryId(1L);

        VillageGroundCategory existingVillageGroundCategory = new VillageGroundCategory();
        existingVillageGroundCategory.setId(id);
        when(villageGroundCategoryRepository.findById(id)).thenReturn(Optional.of(existingVillageGroundCategory));

        VillageGroundCategory updatedVillageGroundCategory = new VillageGroundCategory();
        updatedVillageGroundCategory.setId(id);
        updatedVillageGroundCategory.setVillage(village);
        updatedVillageGroundCategory.setGroundCategory(groundCategory);
        when(villageGroundCategoryRepository.save(existingVillageGroundCategory)).thenReturn(updatedVillageGroundCategory);

        VillageGroundCategoryDTO expectedDTO = new VillageGroundCategoryDTO();
        expectedDTO.setId(id);
        expectedDTO.setVillageId(1L);
        expectedDTO.setGroundCategoryId(1L);
        when(modelMapper.map(updatedVillageGroundCategory, VillageGroundCategoryDTO.class)).thenReturn(expectedDTO);

        VillageGroundCategoryDTO resultDTO = villageGroundCategoryService.updateVillageGroundCategory(id, inputDTO);

        assertEquals(id, resultDTO.getId());
        assertEquals(inputDTO.getVillageId(), resultDTO.getVillageId());
        assertEquals(inputDTO.getGroundCategoryId(), resultDTO.getGroundCategoryId());
    }

    @Test
    void testDeleteVillageGroundCategoryWhenIdExists() {
        Long id = 1L;
        doNothing().when(villageGroundCategoryRepository).deleteById(id);

        int result = villageGroundCategoryService.deleteVillageGroundCategory(id);

        assertEquals(1, result);
        verify(villageGroundCategoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteVillageGroundCategoryWhenIdDoesNotExist() {
        Long id = 1L;
        doThrow(EmptyResultDataAccessException.class).when(villageGroundCategoryRepository).deleteById(id);

        int result = villageGroundCategoryService.deleteVillageGroundCategory(id);

        assertEquals(0, result);
        verify(villageGroundCategoryRepository, times(1)).deleteById(id);
    }

}
