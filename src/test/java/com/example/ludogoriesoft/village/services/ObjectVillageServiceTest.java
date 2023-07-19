package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.enums.Distance;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.model.ObjectVillage;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.ObjectAroundVillageRepository;
import com.example.ludogorieSoft.village.repositories.ObjectVillageRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectVillageServiceTest {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectAroundVillageRepository objectAroundVillageRepository;

    @Mock
    private VillageRepository villageRepository;

    @Mock
    private ObjectVillageRepository objectVillageRepository;

    @Mock
    private VillageService villageService;

    @Mock
    private ObjectAroundVillageService objectAroundVillageService;

    @InjectMocks
    private ObjectVillageService objectVillageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObjectVillageToObjectVillageDTO() {
        ObjectVillage objectVillage = new ObjectVillage();
        ObjectVillageDTO expectedDTO = new ObjectVillageDTO();
        when(modelMapper.map(objectVillage, ObjectVillageDTO.class)).thenReturn(expectedDTO);

        ObjectVillageDTO resultDTO = objectVillageService.objectVillageToObjectVillageDTO(objectVillage);

        assertEquals(expectedDTO, resultDTO);
        verify(modelMapper).map(objectVillage, ObjectVillageDTO.class);
    }

    @Test
    void testGetAllObjectVillages() {
        List<ObjectVillage> objectVillages = new ArrayList<>();
        objectVillages.add(new ObjectVillage());
        when(objectVillageRepository.findAll()).thenReturn(objectVillages);

        List<ObjectVillageDTO> resultDTOs = objectVillageService.getAllObjectVillages();

        assertEquals(objectVillages.size(), resultDTOs.size());
        verify(objectVillageRepository, times(1)).findAll();
        verify(modelMapper, times(objectVillages.size())).map(any(), eq(ObjectVillageDTO.class));
    }

    @Test
    void testGetObjectVillageById() {
        Long objectId = 1L;
        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(objectId);
        ObjectVillage objectVillage = new ObjectVillage();
        objectVillage.setId(objectId);
        when(objectVillageRepository.findById(objectId)).thenReturn(Optional.of(objectVillage));
        when(modelMapper.map(objectVillage, ObjectVillageDTO.class)).thenReturn(objectVillageDTO);

        ObjectVillageDTO resultDTO = objectVillageService.getObjectVillageById(objectId);

        assertDoesNotThrow(() -> objectVillageService.getObjectVillageById(objectId));

        assertNotNull(resultDTO);
        assertEquals(objectId, resultDTO.getId());
        verify(objectVillageRepository, times(2)).findById(objectId);
    }

    @Test
    void testUpdateObjectVillageById() {
        Long objectId = 1L;
        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(objectId);
        ObjectVillage objectVillage = new ObjectVillage();
        objectVillage.setId(objectId);
        when(objectVillageRepository.findById(objectId)).thenReturn(Optional.of(objectVillage));
        when(villageService.checkVillage(any())).thenReturn(new Village());
        when(objectAroundVillageService.checkObject(any())).thenReturn(new ObjectAroundVillage());

        ObjectVillageDTO resultDTO = objectVillageService.updateObjectVillageById(objectId, objectVillageDTO);

        assertDoesNotThrow(() -> objectVillageService.updateObjectVillageById(objectId, objectVillageDTO));

        assertNotNull(resultDTO);
        assertEquals(objectId, resultDTO.getId());
        verify(objectVillageRepository, times(2)).findById(objectId);
        verify(villageService, times(2)).checkVillage(objectVillageDTO.getVillageId());
        verify(objectAroundVillageService, times(2)).checkObject(objectVillageDTO.getObjectAroundVillageId());
        verify(objectVillageRepository, times(2)).save(objectVillage);
    }

    @Test
    void testUpdateObjectVillageByIdWhenObjectVillageDoesNotExist() {
        Long objectId = 1L;
        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(objectId);
        when(objectVillageRepository.findById(objectId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> objectVillageService.updateObjectVillageById(objectId, objectVillageDTO));
        verify(objectVillageRepository, times(1)).findById(objectId);
        verifyNoMoreInteractions(villageService, objectAroundVillageService, objectVillageRepository);
    }

    @Test
    void testGetObjectVillageByIdWhenObjectVillageDoesNotExist() {
        Long objectId = 1L;
        when(objectVillageRepository.findById(objectId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> objectVillageService.getObjectVillageById(objectId));
        verify(objectVillageRepository, times(1)).findById(objectId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testCreateObjectVillage() {
        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        when(villageService.checkVillage(any())).thenReturn(new Village());
        when(objectAroundVillageService.checkObject(any())).thenReturn(new ObjectAroundVillage());

        ObjectVillageDTO resultDTO = objectVillageService.createObjectVillage(objectVillageDTO);

        assertNotNull(resultDTO);
        verify(villageService, times(1)).checkVillage(objectVillageDTO.getVillageId());
        verify(objectAroundVillageService, times(1)).checkObject(objectVillageDTO.getObjectAroundVillageId());
        verify(objectVillageRepository, times(1)).save(any(ObjectVillage.class));
    }

    @Test
    void testDeleteObjectVillageById() {
        Long objectId = 1L;
        ObjectVillage objectVillage = new ObjectVillage();
        objectVillage.setId(objectId);
        when(objectVillageRepository.findById(objectId)).thenReturn(Optional.of(objectVillage));

        assertDoesNotThrow(() -> objectVillageService.deleteObjectVillageById(objectId));

        verify(objectVillageRepository, times(1)).findById(objectId);
        verify(objectVillageRepository, times(1)).delete(objectVillage);
    }

    @Test
    void testDeleteObjectVillageById_WhenObjectVillageDoesNotExist() {
        Long objectId = 1L;
        when(objectVillageRepository.findById(objectId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> objectVillageService.deleteObjectVillageById(objectId));
        verify(objectVillageRepository).findById(objectId);
        verifyNoMoreInteractions(objectVillageRepository);
    }

    @Test
    void getObjectVillageByVillageId_ValidId_ReturnsFilteredObjectVillageDTOList() {
        // Arrange
        Long villageId = 1L;
        Village village = new Village();
        village.setId(villageId);

        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setId(1L);
        objectVillage1.setVillage(village);
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setId(2L);
        objectVillage2.setVillage(village);
        List<ObjectVillage> objectVillageList = Arrays.asList(objectVillage1, objectVillage2);

        ObjectVillageDTO objectVillageDTO1 = new ObjectVillageDTO();
        objectVillageDTO1.setId(1L);
        ObjectVillageDTO objectVillageDTO2 = new ObjectVillageDTO();
        objectVillageDTO2.setId(2L);
        List<ObjectVillageDTO> expectedObjectVillageDTOList = Arrays.asList(objectVillageDTO1, objectVillageDTO2);

        when(objectVillageRepository.findAll()).thenReturn(objectVillageList);
        when(modelMapper.map(objectVillage1, ObjectVillageDTO.class)).thenReturn(objectVillageDTO1);
        when(modelMapper.map(objectVillage2, ObjectVillageDTO.class)).thenReturn(objectVillageDTO2);

        // Act
        List<ObjectVillageDTO> result = objectVillageService.getObjectVillageByVillageId(villageId);

        // Assert
        assertEquals(expectedObjectVillageDTOList, result);
        verify(objectVillageRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(objectVillage1, ObjectVillageDTO.class);
        verify(modelMapper, times(1)).map(objectVillage2, ObjectVillageDTO.class);
    }

    @Test
    void testExistsByVillageIdAndObjectIdAndDistanceWhenExistsThenReturnsTrue() {
        Long villageId = 1L;
        Long objectId = 2L;
        Distance distance = Distance.IN_THE_VILLAGE;

        when(objectVillageRepository.existsByVillageIdAndObjectIdAndDistance(villageId, objectId, distance)).thenReturn(true);

        boolean result = objectVillageService.existsByVillageIdAndObjectIdAndDistance(villageId, objectId, distance);

        assertTrue(result);
        verify(objectVillageRepository, times(1)).existsByVillageIdAndObjectIdAndDistance(villageId, objectId, distance);
    }

    @Test
    void testExistsByVillageIdAndObjectIdAndDistanceWhenNotExistsThenReturnsFalse() {
        Long villageId = 1L;
        Long objectId = 2L;
        Distance distance = Distance.ON_10_KM;

        when(objectVillageRepository.existsByVillageIdAndObjectIdAndDistance(villageId, objectId, distance)).thenReturn(false);

        boolean result = objectVillageService.existsByVillageIdAndObjectIdAndDistance(villageId, objectId, distance);

        assertFalse(result);
        verify(objectVillageRepository, times(1)).existsByVillageIdAndObjectIdAndDistance(villageId, objectId, distance);
    }

}
