package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.VillageGroundCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VillageGroundCategoryServiceTest {
    @Mock
    private VillageGroundCategoryRepository villageGroundCategoryRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @Mock
    private GroundCategoryService groundCategoryService;

    @Captor
    private ArgumentCaptor<List<VillageGroundCategory>> groundCategoryListCaptor;
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
    void testCreateVillageGroundCategoryDTOValidInputReturnsExpectedDTO() {
        VillageGroundCategoryDTO inputDTO = new VillageGroundCategoryDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setGroundCategoryId(1L);
        VillageGroundCategory savedVillageGroundCategory = new VillageGroundCategory();

        Village village = new Village();
        village.setId(1L);
        when(villageService.checkVillage(1L)).thenReturn(village);
        savedVillageGroundCategory.setVillage(villageService.checkVillage(1L));

        GroundCategory groundCategory = new GroundCategory();
        groundCategory.setId(1L);
        when(groundCategoryService.checkGroundCategory(1L)).thenReturn(groundCategory);
        savedVillageGroundCategory.setGroundCategory(groundCategoryService.checkGroundCategory(1L));
        savedVillageGroundCategory.setVillage(villageService.checkVillage(1L));

        when(villageGroundCategoryRepository.save(any(VillageGroundCategory.class))).thenReturn(savedVillageGroundCategory);
        when(villageGroundCategoryService.toDTO(savedVillageGroundCategory)).thenReturn(inputDTO);

        VillageGroundCategoryDTO expectedDTO = new VillageGroundCategoryDTO();
        expectedDTO.setId(savedVillageGroundCategory.getId());
        expectedDTO.setVillageId(savedVillageGroundCategory.getVillage().getId());
        expectedDTO.setGroundCategoryId(savedVillageGroundCategory.getGroundCategory().getId());

        VillageGroundCategoryDTO resultDTO = villageGroundCategoryService.createVillageGroundCategoryDTO(inputDTO);

        assertNotNull(resultDTO);
        assertEquals(expectedDTO.getId(), resultDTO.getId());
        assertEquals(expectedDTO.getVillageId(), resultDTO.getVillageId());
        assertEquals(expectedDTO.getGroundCategoryId(), resultDTO.getGroundCategoryId());

        verify(villageService, times(3)).checkVillage(1L);
        verify(groundCategoryService, times(2)).checkGroundCategory(1L);
        verify(villageGroundCategoryRepository, times(1)).save(any(VillageGroundCategory.class));
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


    @Test
    void testGetAllVillageGroundCategoriesWhenNoneExist() {
        when(villageGroundCategoryRepository.findAll()).thenReturn(new ArrayList<>());

        List<VillageGroundCategoryDTO> resultDTOs = villageGroundCategoryService.getAllVillageGroundCategories();

        assertTrue(resultDTOs.isEmpty());
    }


    @Test
    void testCreateVillageGroundCategoryDTOInvalidVillage() {
        VillageGroundCategoryDTO inputDTO = new VillageGroundCategoryDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setGroundCategoryId(1L);

        when(villageService.checkVillage(1L)).thenThrow(ApiRequestException.class);

        assertThrows(ApiRequestException.class, () -> villageGroundCategoryService.createVillageGroundCategoryDTO(inputDTO));

        verify(villageService, times(1)).checkVillage(1L);
        verify(groundCategoryService, never()).checkGroundCategory(anyLong());
        verify(villageGroundCategoryRepository, never()).save(any(VillageGroundCategory.class));
    }

    @Test
    void testCreateVillageGroundCategoryDTOInvalidGroundCategory() {
        VillageGroundCategoryDTO inputDTO = new VillageGroundCategoryDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setGroundCategoryId(1L);

        Village village = new Village();
        village.setId(1L);
        when(villageService.checkVillage(1L)).thenReturn(village);

        when(groundCategoryService.checkGroundCategory(1L)).thenThrow(ApiRequestException.class);

        assertThrows(ApiRequestException.class, () -> villageGroundCategoryService.createVillageGroundCategoryDTO(inputDTO));

        verify(villageService, times(1)).checkVillage(1L);
        verify(groundCategoryService, times(1)).checkGroundCategory(1L);
        verify(villageGroundCategoryRepository, never()).save(any(VillageGroundCategory.class));
    }

    @Test
    void testUpdateVillageGroundCategoryWhenIdDoesNotExist() {
        Long id = 1L;
        VillageGroundCategoryDTO inputDTO = new VillageGroundCategoryDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setGroundCategoryId(1L);

        when(villageGroundCategoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageGroundCategoryService.updateVillageGroundCategory(id, inputDTO));

        verify(villageGroundCategoryRepository, times(1)).findById(id);
        verify(villageService, never()).checkVillage(anyLong());
        verify(groundCategoryService, never()).checkGroundCategory(anyLong());
        verify(villageGroundCategoryRepository, never()).save(any(VillageGroundCategory.class));
    }

    @Test
    void testFindVillageGroundCategoryDTOByVillageIdWhenExists() {
        Long villageId = 1L;
        VillageGroundCategory villageGroundCategory = new VillageGroundCategory();
        Village village = new Village();
        village.setId(1L);
        villageGroundCategory.setVillage(village);
        villageGroundCategory.setGroundCategory(new GroundCategory(1L, "test ground category"));

        VillageGroundCategoryDTO expectedDTO = new VillageGroundCategoryDTO();
        expectedDTO.setVillageId(1L);
        expectedDTO.setGroundCategoryId(1L);

        when(villageGroundCategoryRepository.findByVillageId(villageId)).thenReturn(villageGroundCategory);
        when(villageGroundCategoryService.toDTO(villageGroundCategory)).thenReturn(expectedDTO);
        VillageGroundCategoryDTO resultDTO = villageGroundCategoryService.findVillageGroundCategoryDTOByVillageId(villageId);

        assertNotNull(resultDTO);
        assertEquals(villageGroundCategory.getVillage().getId(), resultDTO.getVillageId());
        assertEquals(villageGroundCategory.getGroundCategory().getId(), resultDTO.getGroundCategoryId());
    }

    @Test
    void testFindVillageGroundCategoryDTOByVillageIdWhenNotExists() {
        Long villageId = 1L;
        when(villageGroundCategoryRepository.findByVillageId(villageId)).thenReturn(null);
        assertThrows(ApiRequestException.class, () -> {
            villageGroundCategoryService.findVillageGroundCategoryDTOByVillageId(villageId);
        });
    }

    @Test
    void testUpdateVillageGroundCategoryStatus() {
        Long villageId = 1L;
        String localDateTime = "2023-08-10T00:00:00";
        boolean status = true;

        Village village = new Village();
        village.setId(villageId);

        VillageGroundCategory groundCategory = new VillageGroundCategory();
        groundCategory.setVillage(village);

        List<VillageGroundCategory> groundCategories = new ArrayList<>();
        groundCategories.add(groundCategory);

        when(villageGroundCategoryRepository.findByVillageIdAndVillageStatusAndDateUpload(
                villageId, status, localDateTime
        )).thenReturn(groundCategories);

        villageGroundCategoryService.updateVillageGroundCategoryStatus(villageId, status, localDateTime);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(villageGroundCategoryRepository).saveAll(groundCategoryListCaptor.capture());
    }

    @Test
    void testRejectVillageGroundCategoryResponse() {
        Long villageId = 1L;
        String responseDate = "2023-08-10T00:00:00";
        LocalDateTime deleteDate = LocalDateTime.now();

        Village village = new Village();
        village.setId(villageId);

        VillageGroundCategory groundCategory = new VillageGroundCategory();
        groundCategory.setVillage(village);

        List<VillageGroundCategory> groundCategories = new ArrayList<>();
        groundCategories.add(groundCategory);

        when(villageGroundCategoryRepository.findByVillageIdAndVillageStatusAndDateUpload(
                villageId, true, responseDate
        )).thenReturn(groundCategories);

        villageGroundCategoryService.rejectVillageGroundCategoryResponse(villageId, true, responseDate, deleteDate);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(villageGroundCategoryRepository).saveAll(groundCategoryListCaptor.capture());

    }

}
