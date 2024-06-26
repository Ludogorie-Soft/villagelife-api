package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.GroundCategoryDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.GroundCategory;
import com.example.ludogorieSoft.village.repositories.GroundCategoryRepository;
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
import static org.mockito.Mockito.*;

class GroundCategoryServiceTest {
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private GroundCategoryRepository groundCategoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GroundCategoryService groundCategoryService;

    @Test
    void getAllGroundCategoriesShouldReturnAllGroundCategories() {
        List<GroundCategory> groundCategories = new ArrayList<>();
        groundCategories.add(new GroundCategory());
        groundCategories.add(new GroundCategory());
        groundCategories.add(new GroundCategory());
        when(groundCategoryRepository.findAllByOrderByIdAsc()).thenReturn(groundCategories);
        List<GroundCategoryDTO> result = groundCategoryService.getAllGroundCategories();
        verify(groundCategoryRepository, times(1)).findAllByOrderByIdAsc();
        assertEquals(groundCategories.size(), result.size());
    }

    @Test
    void getAllGroundCategoriesShouldReturnEmptyListWhenNoGroundCategoriesFound() {
        List<GroundCategory> groundCategories = new ArrayList<>();
        when(groundCategoryRepository.findAllByOrderByIdAsc()).thenReturn(groundCategories);
        List<GroundCategoryDTO> result = groundCategoryService.getAllGroundCategories();
        verify(groundCategoryRepository, times(1)).findAllByOrderByIdAsc();
        Assertions.assertTrue(result.isEmpty());
    }
    @Test
    void getByIDShouldReturnExistingGroundCategory() {
        Long groundCategoryId = 1L;
        GroundCategory existingGroundCategory = new GroundCategory();
        existingGroundCategory.setId(groundCategoryId);
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(groundCategoryId);
        Optional<GroundCategory> optionalGroundCategory = Optional.of(existingGroundCategory);

        when(groundCategoryRepository.findById(groundCategoryId)).thenReturn(optionalGroundCategory);
        when(groundCategoryService.toDTO(existingGroundCategory)).thenReturn(groundCategoryDTO);

        GroundCategoryDTO result = groundCategoryService.getByID(groundCategoryId);
        verify(groundCategoryRepository, times(1)).findById(groundCategoryId);
        assertEquals(existingGroundCategory.getId(), result.getId());
    }

    @Test
    void getByIDShouldThrowExceptionWhenGroundCategoryNotFound() {
        Long groundCategoryId = 1L;
        Optional<GroundCategory> optionalGroundCategory = Optional.empty();

        when(groundCategoryRepository.findById(groundCategoryId)).thenReturn(optionalGroundCategory);

        assertThrows(ApiRequestException.class, () -> groundCategoryService.getByID(groundCategoryId));
        verify(groundCategoryRepository, times(1)).findById(groundCategoryId);
    }
    @Test
    void createGroundCategoryDTOShouldReturnCreatedGroundCategoryDTO() {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setGroundCategoryName("Test Ground Category");

        when(groundCategoryRepository.existsByGroundCategoryName(groundCategoryDTO.getGroundCategoryName())).thenReturn(false);

        GroundCategory savedGroundCategory = new GroundCategory();
        savedGroundCategory.setGroundCategoryName(groundCategoryDTO.getGroundCategoryName());
        when(groundCategoryRepository.save(any(GroundCategory.class))).thenReturn(savedGroundCategory);
        when(groundCategoryService.toDTO(savedGroundCategory)).thenReturn(groundCategoryDTO);

        GroundCategoryDTO result = groundCategoryService.createGroundCategoryDTO(groundCategoryDTO);

        verify(groundCategoryRepository, times(1)).existsByGroundCategoryName(groundCategoryDTO.getGroundCategoryName());
        verify(groundCategoryRepository, times(1)).save(any(GroundCategory.class));
        assertEquals(groundCategoryDTO.getGroundCategoryName(), result.getGroundCategoryName());
    }

    @Test
    void createGroundCategoryDTOShouldThrowExceptionWhenGroundCategoryNameExists() {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setGroundCategoryName("Test Ground Category");

        when(groundCategoryRepository.existsByGroundCategoryName(groundCategoryDTO.getGroundCategoryName())).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> groundCategoryService.createGroundCategoryDTO(groundCategoryDTO));

        verify(groundCategoryRepository, times(1)).existsByGroundCategoryName(groundCategoryDTO.getGroundCategoryName());
        verify(groundCategoryRepository, never()).save(any(GroundCategory.class));
    }
    @Test
    void updateGroundCategoryShouldReturnUpdatedGroundCategoryDTO() {
        Long groundCategoryId = 1L;
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(groundCategoryId);
        groundCategoryDTO.setGroundCategoryName("Updated Ground Category");

        GroundCategory existingGroundCategory = new GroundCategory();
        existingGroundCategory.setId(groundCategoryId);
        existingGroundCategory.setGroundCategoryName("Original Ground Category");

        when(groundCategoryRepository.findById(groundCategoryId)).thenReturn(Optional.of(existingGroundCategory));

        GroundCategory savedGroundCategory = new GroundCategory();
        savedGroundCategory.setId(groundCategoryId);
        savedGroundCategory.setGroundCategoryName(groundCategoryDTO.getGroundCategoryName());
        when(groundCategoryRepository.save(any(GroundCategory.class))).thenReturn(savedGroundCategory);
        when(groundCategoryService.toDTO(savedGroundCategory)).thenReturn(groundCategoryDTO);

        GroundCategoryDTO result = groundCategoryService.updateGroundCategory(groundCategoryId, groundCategoryDTO);

        verify(groundCategoryRepository, times(1)).findById(groundCategoryId);
        verify(groundCategoryRepository, times(1)).save(any(GroundCategory.class));
        assertEquals(groundCategoryId, result.getId());
        assertEquals(groundCategoryDTO.getGroundCategoryName(), result.getGroundCategoryName());
    }


    @Test
    void updateGroundCategoryShouldThrowExceptionWhenGroundCategoryNotFound() {
        Long groundCategoryId = 1L;
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setGroundCategoryName("Updated Ground Category");

        when(groundCategoryRepository.findById(groundCategoryId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> groundCategoryService.updateGroundCategory(groundCategoryId, groundCategoryDTO));

        verify(groundCategoryRepository, times(1)).findById(groundCategoryId);
        verify(groundCategoryRepository, never()).save(any(GroundCategory.class));
    }
    @Test
    void deleteGroundCategoryShouldDeleteExistingGroundCategory() {
        Long groundCategoryId = 1L;
        GroundCategory existingGroundCategory = new GroundCategory();
        existingGroundCategory.setId(groundCategoryId);

        when(groundCategoryRepository.findById(groundCategoryId)).thenReturn(Optional.of(existingGroundCategory));

        groundCategoryService.deleteGroundCategory(groundCategoryId);

        verify(groundCategoryRepository, times(1)).findById(groundCategoryId);
        verify(groundCategoryRepository, times(1)).delete(existingGroundCategory);
    }

    @Test
    void deleteGroundCategoryShouldThrowExceptionWhenGroundCategoryNotFound() {
        Long groundCategoryId = 1L;

        when(groundCategoryRepository.findById(groundCategoryId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> groundCategoryService.deleteGroundCategory(groundCategoryId));

        verify(groundCategoryRepository, times(1)).findById(groundCategoryId);
        verify(groundCategoryRepository, never()).delete(any(GroundCategory.class));
    }
    @Test
    void checkGroundCategoryShouldReturnExistingGroundCategory() {
        Long groundCategoryId = 1L;
        GroundCategory existingGroundCategory = new GroundCategory();
        existingGroundCategory.setId(groundCategoryId);

        when(groundCategoryRepository.findById(groundCategoryId)).thenReturn(Optional.of(existingGroundCategory));

        GroundCategory result = groundCategoryService.checkGroundCategory(groundCategoryId);

        verify(groundCategoryRepository, times(1)).findById(groundCategoryId);
        assertEquals(existingGroundCategory, result);
    }

    @Test
    void checkGroundCategoryShouldThrowExceptionWhenGroundCategoryNotFound() {
        Long groundCategoryId = 1L;

        when(groundCategoryRepository.findById(groundCategoryId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> groundCategoryService.checkGroundCategory(groundCategoryId));

        verify(groundCategoryRepository, times(1)).findById(groundCategoryId);
    }


    @Test
    void getByGroundCategoryNameShouldReturnExistingGroundCategory() {
        String groundCategoryName = "Test Ground Category";
        GroundCategory existingGroundCategory = new GroundCategory();
        existingGroundCategory.setId(1L);
        existingGroundCategory.setGroundCategoryName(groundCategoryName);
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(existingGroundCategory.getId());
        groundCategoryDTO.setGroundCategoryName(existingGroundCategory.getGroundCategoryName());

        when(groundCategoryRepository.findByGroundCategoryName(groundCategoryName)).thenReturn(existingGroundCategory);
        when(groundCategoryService.toDTO(existingGroundCategory)).thenReturn(groundCategoryDTO);

        GroundCategoryDTO result = groundCategoryService.getByGroundCategoryName(groundCategoryName);
        assertEquals(existingGroundCategory.getId(), result.getId());
        assertEquals(existingGroundCategory.getGroundCategoryName(), result.getGroundCategoryName());
    }

    @Test
    void getByGroundCategoryNameShouldThrowExceptionWhenGroundCategoryNotFound() {
        String groundCategoryName = "Non-Existent Ground Category";
        when(groundCategoryRepository.findByGroundCategoryName(groundCategoryName)).thenReturn(null);

        assertThrows(ApiRequestException.class, () -> groundCategoryService.getByGroundCategoryName(groundCategoryName));

        verify(groundCategoryRepository, times(1)).findByGroundCategoryName(groundCategoryName);
    }

    @Test
    void createGroundCategoryDTOShouldThrowExceptionWhenGroundCategoryNameIsBlank() {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setGroundCategoryName("");

        assertThrows(ApiRequestException.class, () -> groundCategoryService.createGroundCategoryDTO(groundCategoryDTO));

        verify(groundCategoryRepository, never()).existsByGroundCategoryName(groundCategoryDTO.getGroundCategoryName());
        verify(groundCategoryRepository, never()).save(any(GroundCategory.class));
    }

    @Test
    void testFindGroundCategoryByNameExistingCategory() {
        GroundCategory existingCategory = new GroundCategory();
        existingCategory.setId(1L);
        existingCategory.setGroundCategoryName("U+043D U+0435 U+0020 U+0437 U+043D U+0430 U+043C.");

        GroundCategoryDTO groundCategory = new GroundCategoryDTO();
        groundCategory.setId(1L);
        groundCategory.setGroundCategoryName("U+043D U+0435 U+0020 U+0437 U+043D U+0430 U+043C.");

        when(groundCategoryRepository.findByGroundCategoryName("U+043D U+0435 U+0020 U+0437 U+043D U+0430 U+043C."))
                .thenReturn(existingCategory);
        when(groundCategoryService.toDTO(existingCategory)).thenReturn(groundCategory);

        GroundCategoryDTO result = groundCategoryService.findGroundCategoryByName("U+043D U+0435 U+0020 U+0437 U+043D U+0430 U+043C.");

        assertNotNull(result);
        assertEquals(existingCategory.getId(), result.getId());
        assertEquals(existingCategory.getGroundCategoryName(), result.getGroundCategoryName());

        verify(groundCategoryRepository, times(1)).findByGroundCategoryName("U+043D U+0435 U+0020 U+0437 U+043D U+0430 U+043C.");
    }

    @Test
    void testFindGroundCategoryByName_NonExistingCategory() {
        when(groundCategoryRepository.findByGroundCategoryName("NON EXISTING CATEGORY NAME"))
                .thenReturn(null);

        assertThrows(ApiRequestException.class, () -> groundCategoryService.findGroundCategoryByName("NON EXISTING CATEGORY NAME"));

        verify(groundCategoryRepository, times(1)).findByGroundCategoryName("NON EXISTING CATEGORY NAME");
    }
}
