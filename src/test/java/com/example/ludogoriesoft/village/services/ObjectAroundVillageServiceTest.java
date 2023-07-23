package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.repositories.ObjectAroundVillageRepository;
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

class ObjectAroundVillageServiceTest {
    @Mock
    private ObjectAroundVillageRepository objectAroundVillageRepository;

    @InjectMocks
    private ObjectAroundVillageService objectAroundVillageService;
    @Mock
    private ModelMapper modelMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        objectAroundVillageService = new ObjectAroundVillageService(objectAroundVillageRepository, modelMapper);
    }

    @Test
    void testGetAllObjectsAroundVillageWithMultipleObjects() {
        List<ObjectAroundVillage> objectAroundVillages = new ArrayList<>();
        objectAroundVillages.add(new ObjectAroundVillage(1L, "Object 1"));
        objectAroundVillages.add(new ObjectAroundVillage(2L, "Object 2"));

        when(objectAroundVillageRepository.findAllByOrderByIdAsc()).thenReturn(objectAroundVillages);

        List<ObjectAroundVillageDTO> result = objectAroundVillageService.getAllObjectsAroundVillage();

        verify(objectAroundVillageRepository, times(1)).findAllByOrderByIdAsc();
        Assertions.assertEquals(objectAroundVillages.size(), result.size());
    }

    @Test
    void testGetAllObjectsAroundVillageWithNoObjects() {
        List<ObjectAroundVillage> objectAroundVillages = new ArrayList<>();
        when(objectAroundVillageRepository.findAllByOrderByIdAsc()).thenReturn(objectAroundVillages);
        List<ObjectAroundVillageDTO> result = objectAroundVillageService.getAllObjectsAroundVillage();
        verify(objectAroundVillageRepository, times(1)).findAllByOrderByIdAsc();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetObjectAroundVillageByIdWithExistingId() {
        Long objectId = 123L;
        ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage(objectId, "Object 1");
        ObjectAroundVillageDTO expectedDTO = new ObjectAroundVillageDTO(objectId, "Object 1");
        when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.of(objectAroundVillage));
        when(modelMapper.map(objectAroundVillage, ObjectAroundVillageDTO.class)).thenReturn(expectedDTO);
        ObjectAroundVillageDTO result = objectAroundVillageService.getObjectAroundVillageById(objectId);

        verify(objectAroundVillageRepository, times(1)).findById(objectId);
        Assertions.assertEquals(objectAroundVillage.getId(), result.getId());
    }

    @Test
    void testGetObjectAroundVillageByIdWithNonExistingId() {
        Long objectId = 123L;
        when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class,
                () -> objectAroundVillageService.getObjectAroundVillageById(objectId));
        verify(objectAroundVillageRepository, times(1)).findById(objectId);
    }

    @Test
    void testCreateObjectAroundVillageWithNonExistingType() {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("Object 1");

        when(objectAroundVillageRepository.existsByType(objectAroundVillageDTO.getType())).thenReturn(false);

        ObjectAroundVillageDTO result = objectAroundVillageService.createObjectAroundVillage(objectAroundVillageDTO);

        verify(objectAroundVillageRepository, times(1)).existsByType(objectAroundVillageDTO.getType());
        verify(objectAroundVillageRepository, times(1)).save(any(ObjectAroundVillage.class));
        Assertions.assertEquals(objectAroundVillageDTO, result);
    }

    @Test
    void testCreateObjectAroundVillageWithExistingType() {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("Object 1");

        when(objectAroundVillageRepository.existsByType(objectAroundVillageDTO.getType())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class,
                () -> objectAroundVillageService.createObjectAroundVillage(objectAroundVillageDTO));
        verify(objectAroundVillageRepository, times(1)).existsByType(objectAroundVillageDTO.getType());
        verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    }


    @Test
    void testCreateObjectAroundVillage() {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("Test Type");
        ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage();
        objectAroundVillage.setType("Test Type");

        when(objectAroundVillageRepository.existsByType("Test Type")).thenReturn(false);
        when(modelMapper.map(objectAroundVillage, ObjectAroundVillageDTO.class)).thenReturn(objectAroundVillageDTO);
        when(objectAroundVillageRepository.save(any(ObjectAroundVillage.class))).thenReturn(objectAroundVillage);

        ObjectAroundVillageDTO result = objectAroundVillageService.createObjectAroundVillage(objectAroundVillageDTO);

        assertNotNull(result);
        assertEquals("Test Type", result.getType());
        verify(objectAroundVillageRepository, times(1)).existsByType("Test Type");
        verify(objectAroundVillageRepository, times(1)).save(any(ObjectAroundVillage.class));
    }

    @Test
    void testCreateObjectAroundVillageExistingType() {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("Test Type");

        when(objectAroundVillageRepository.existsByType("Test Type")).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> objectAroundVillageService.createObjectAroundVillage(objectAroundVillageDTO));

        verify(objectAroundVillageRepository, times(1)).existsByType("Test Type");
        verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    }

    @Test
    void testUpdateObjectAroundVillage() {
        Long objectId = 1L;
        String updatedType = "Updated Type";
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType(updatedType);

        ObjectAroundVillage existingObject = new ObjectAroundVillage();
        existingObject.setId(objectId);
        existingObject.setType("Old Type");

        when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.of(existingObject));
        when(objectAroundVillageRepository.existsByType(updatedType)).thenReturn(false);
        when(objectAroundVillageRepository.save(existingObject)).thenReturn(existingObject);

        ObjectAroundVillageDTO updatedObject = objectAroundVillageService.updateObjectAroundVillage(objectId, objectAroundVillageDTO);

        assertNotNull(updatedObject);
        assertEquals(updatedType, updatedObject.getType());
        verify(objectAroundVillageRepository, times(1)).findById(objectId);
        verify(objectAroundVillageRepository, times(1)).existsByType(updatedType);
        verify(objectAroundVillageRepository, times(1)).save(existingObject);
    }

    @Test
    void testUpdateObjectAroundVillageInvalidId() {
        Long id = 1L;
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("Updated Type");

        when(objectAroundVillageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> objectAroundVillageService.updateObjectAroundVillage(id, objectAroundVillageDTO));

        verify(objectAroundVillageRepository, times(1)).findById(id);
        verify(objectAroundVillageRepository, never()).existsByType(anyString());
        verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    }

    @Test
    void testUpdateObjectAroundVillageExistingType() {
        Long id = 1L;
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("Updated Type");
        ObjectAroundVillage existingObjectAroundVillage = new ObjectAroundVillage();
        existingObjectAroundVillage.setId(id);
        existingObjectAroundVillage.setType("Old Type");

        Optional<ObjectAroundVillage> optionalObjectAroundVillage = Optional.of(existingObjectAroundVillage);

        when(objectAroundVillageRepository.findById(id)).thenReturn(optionalObjectAroundVillage);
        when(objectAroundVillageRepository.existsByType("Updated Type")).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> objectAroundVillageService.updateObjectAroundVillage(id, objectAroundVillageDTO));

        verify(objectAroundVillageRepository, times(1)).findById(id);
        verify(objectAroundVillageRepository, times(1)).existsByType("Updated Type");
        verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    }


    @Test
    void testDeleteObjectAroundVillageByIdWithExistingId() {
        Long objectId = 123L;
        ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage();
        when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.of(objectAroundVillage));

        objectAroundVillageService.deleteObjectAroundVillageById(objectId);

        verify(objectAroundVillageRepository, times(1)).findById(objectId);
        verify(objectAroundVillageRepository, times(1)).delete(objectAroundVillage);
    }

    @Test
    void testDeleteObjectAroundVillageByIdWithNonExistingId() {
        Long objectId = 123L;
        when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.empty());

        ApiRequestException exception = Assertions.assertThrows(ApiRequestException.class,
                () -> objectAroundVillageService.deleteObjectAroundVillageById(objectId));

        Assertions.assertEquals("Object Around Village not found for id " + objectId, exception.getMessage());
        verify(objectAroundVillageRepository, times(1)).findById(objectId);
        verify(objectAroundVillageRepository, never()).delete(any(ObjectAroundVillage.class));
    }
    @Test
    void checkObjectShouldReturnExistingObject() {
        Long objectId = 1L;
        ObjectAroundVillage existingObject = new ObjectAroundVillage();
        existingObject.setId(objectId);
        Optional<ObjectAroundVillage> optionalObject = Optional.of(existingObject);

        when(objectAroundVillageRepository.findById(objectId)).thenReturn(optionalObject);

        ObjectAroundVillage result = objectAroundVillageService.checkObject(objectId);

        verify(objectAroundVillageRepository, times(1)).findById(objectId);
        assertEquals(existingObject, result);
    }

    @Test
    void checkObjectShouldThrowExceptionWhenObjectNotFound() {
        Long objectId = 1L;
        Optional<ObjectAroundVillage> optionalObject = Optional.empty();

        when(objectAroundVillageRepository.findById(objectId)).thenReturn(optionalObject);

        Assertions.assertThrows(ApiRequestException.class, () -> objectAroundVillageService.checkObject(objectId));

        verify(objectAroundVillageRepository, times(1)).findById(objectId);
    }


    @Test
    void testCreateObjectAroundVillageWithBlankType() {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("");

        assertThrows(ApiRequestException.class, () -> objectAroundVillageService.createObjectAroundVillage(objectAroundVillageDTO),
                "Object Around Village is blank");

        verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    }

    @Test
    void testUpdateObjectAroundVillageWithBlankType() {
        Long objectId = 1L;
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("");

        assertThrows(ApiRequestException.class, () -> objectAroundVillageService.updateObjectAroundVillage(objectId, objectAroundVillageDTO),
                "Invalid Landscape data");

        verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    }

    @Test
    void testUpdateObjectAroundVillageWithNullDTO() {
        Long objectId = 1L;
        ObjectAroundVillageDTO objectAroundVillageDTO = null;

        assertThrows(ApiRequestException.class, () -> objectAroundVillageService.updateObjectAroundVillage(objectId, objectAroundVillageDTO),
                "Invalid Landscape data");

        verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    }
}
