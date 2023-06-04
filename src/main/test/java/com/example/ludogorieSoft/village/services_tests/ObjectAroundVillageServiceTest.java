package com.example.ludogorieSoft.village.services_tests;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.repositories.ObjectAroundVillageRepository;
import com.example.ludogorieSoft.village.services.ObjectAroundVillageService;
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

import static org.mockito.Mockito.*;

public class ObjectAroundVillageServiceTest {
    @Mock
    private ObjectAroundVillageRepository objectAroundVillageRepository;

    @InjectMocks
    private ObjectAroundVillageService objectAroundVillageService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAllObjectsAroundVillageWithMultipleObjects() {
        List<ObjectAroundVillage> objectAroundVillages = new ArrayList<>();
        objectAroundVillages.add(new ObjectAroundVillage(1L, "Object 1"));
        objectAroundVillages.add(new ObjectAroundVillage(2L, "Object 2"));

        when(objectAroundVillageRepository.findAll()).thenReturn(objectAroundVillages);

        List<ObjectAroundVillageDTO> result = objectAroundVillageService.getAllObjectsAroundVillage();

        verify(objectAroundVillageRepository, times(1)).findAll();
        Assertions.assertEquals(objectAroundVillages.size(), result.size());
    }

    @Test
    public void testGetAllObjectsAroundVillageWithNoObjects() {
        List<ObjectAroundVillage> objectAroundVillages = new ArrayList<>();
        when(objectAroundVillageRepository.findAll()).thenReturn(objectAroundVillages);
        List<ObjectAroundVillageDTO> result = objectAroundVillageService.getAllObjectsAroundVillage();
        verify(objectAroundVillageRepository, times(1)).findAll();
        Assertions.assertTrue(result.isEmpty());
    }
    @Test
    public void testGetObjectAroundVillageByIdWithExistingId() {
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
    public void testGetObjectAroundVillageByIdWithNonExistingId() {
        Long objectId = 123L;
        when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class,
                () -> objectAroundVillageService.getObjectAroundVillageById(objectId));
        verify(objectAroundVillageRepository, times(1)).findById(objectId);
    }
    @Test
    public void testCreateObjectAroundVillageWithNonExistingType() {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("Object 1");

        when(objectAroundVillageRepository.existsByType(objectAroundVillageDTO.getType())).thenReturn(false);

        ObjectAroundVillageDTO result = objectAroundVillageService.createObjectAroundVillage(objectAroundVillageDTO);

        verify(objectAroundVillageRepository, times(1)).existsByType(objectAroundVillageDTO.getType());
        verify(objectAroundVillageRepository, times(1)).save(any(ObjectAroundVillage.class));
        Assertions.assertEquals(objectAroundVillageDTO, result);
    }

    @Test
    public void testCreateObjectAroundVillageWithExistingType() {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setType("Object 1");

        when(objectAroundVillageRepository.existsByType(objectAroundVillageDTO.getType())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class,
                () -> objectAroundVillageService.createObjectAroundVillage(objectAroundVillageDTO));
        verify(objectAroundVillageRepository, times(1)).existsByType(objectAroundVillageDTO.getType());
        verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    }
    //@Test
    //public void testUpdateObjectAroundVillageWithExistingIdAndNonExistingType() {
    //    Long objectId = 123L;
    //    ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage();
    //    objectAroundVillage.setType("Updated Object");
//
    //    Optional<ObjectAroundVillage> optionalObjectAroundVillage = Optional.of(new ObjectAroundVillage());
    //    optionalObjectAroundVillage.get().setType("Old Object");
//
    //    when(objectAroundVillageRepository.findById(objectId)).thenReturn(optionalObjectAroundVillage);
    //    when(objectAroundVillageRepository.existsByType(objectAroundVillage.getType())).thenReturn(false);
    //    when(objectAroundVillageRepository.save(any(ObjectAroundVillage.class))).thenReturn(objectAroundVillage);
//
    //    ObjectAroundVillageDTO result = objectAroundVillageService.updateObjectAroundVillage(objectId, objectAroundVillage);
//
    //    verify(objectAroundVillageRepository, times(1)).findById(objectId);
    //    verify(objectAroundVillageRepository, times(1)).existsByType(objectAroundVillage.getType());
    //    verify(objectAroundVillageRepository, times(1)).save(any(ObjectAroundVillage.class));
    //    Assertions.assertEquals(objectAroundVillageService.convertToDTO(objectAroundVillage), result);
    //}
//
    //@Test
    //public void testUpdateObjectAroundVillageWithNonExistingId() {
    //    Long objectId = 123L;
    //    ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage();
    //    objectAroundVillage.setType("Updated Object");
//
    //    when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.empty());
//
    //    Assertions.assertThrows(ApiRequestException.class,
    //            () -> objectAroundVillageService.updateObjectAroundVillage(objectId, objectAroundVillage));
    //    verify(objectAroundVillageRepository, times(1)).findById(objectId);
    //    verify(objectAroundVillageRepository, never()).existsByType(objectAroundVillage.getType());
    //    verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    //}
    //@Test
    //public void testUpdateObjectAroundVillageWithExistingType() {
    //    Long objectId = 123L;
    //    ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage();
    //    objectAroundVillage.setType("Updated Object");
//
    //    Optional<ObjectAroundVillage> optionalObjectAroundVillage = Optional.of(new ObjectAroundVillage());
    //    optionalObjectAroundVillage.get().setType("Old Object");
//
    //    when(objectAroundVillageRepository.findById(objectId)).thenReturn(optionalObjectAroundVillage);
    //    when(objectAroundVillageRepository.existsByType(objectAroundVillage.getType())).thenReturn(true);
//
    //    ApiRequestException exception = Assertions.assertThrows(ApiRequestException.class,
    //            () -> objectAroundVillageService.updateObjectAroundVillage(objectId, objectAroundVillage));
//
    //    Assertions.assertEquals("Object Around Village with type: Updated Object already exists", exception.getMessage());
    //    verify(objectAroundVillageRepository, times(1)).findById(objectId);
    //    verify(objectAroundVillageRepository, times(1)).existsByType(objectAroundVillage.getType());
    //    verify(objectAroundVillageRepository, never()).save(any(ObjectAroundVillage.class));
    //}
    @Test
    public void testDeleteObjectAroundVillageByIdWithExistingId() {
        Long objectId = 123L;
        ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage();
        when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.of(objectAroundVillage));

        objectAroundVillageService.deleteObjectAroundVillageById(objectId);

        verify(objectAroundVillageRepository, times(1)).findById(objectId);
        verify(objectAroundVillageRepository, times(1)).delete(objectAroundVillage);
    }

    @Test
    public void testDeleteObjectAroundVillageByIdWithNonExistingId() {
        Long objectId = 123L;
        when(objectAroundVillageRepository.findById(objectId)).thenReturn(Optional.empty());

        ApiRequestException exception = Assertions.assertThrows(ApiRequestException.class,
                () -> objectAroundVillageService.deleteObjectAroundVillageById(objectId));

        Assertions.assertEquals("Object Around Village not found for id " + objectId, exception.getMessage());
        verify(objectAroundVillageRepository, times(1)).findById(objectId);
        verify(objectAroundVillageRepository, never()).delete(any(ObjectAroundVillage.class));
    }
}
