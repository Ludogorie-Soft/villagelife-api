package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.dtos.response.ObjectVillageResponse;
import com.example.ludogorieSoft.village.enums.Distance;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.model.ObjectVillage;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.ObjectVillageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectVillageServiceTest {
    @Mock
    private ModelMapper modelMapper;

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
    void testDeleteObjectVillageByIdWhenObjectVillageDoesNotExist() {
        Long objectId = 1L;
        when(objectVillageRepository.findById(objectId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> objectVillageService.deleteObjectVillageById(objectId));
        verify(objectVillageRepository).findById(objectId);
        verifyNoMoreInteractions(objectVillageRepository);
    }

    @Test
    void getObjectVillageByVillageIdValidIdReturnsFilteredObjectVillageDTOList() {
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

        List<ObjectVillageDTO> result = objectVillageService.getObjectVillageByVillageId(villageId);

        assertEquals(expectedObjectVillageDTOList, result);
        verify(objectVillageRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(objectVillage1, ObjectVillageDTO.class);
        verify(modelMapper, times(1)).map(objectVillage2, ObjectVillageDTO.class);
    }

    @Test
    void testGetObjectVillageByVillageIdWithValidId() {
        Long villageId = 1L;

        List<ObjectVillage> objectVillages = new ArrayList<>();
        objectVillages.add(new ObjectVillage());
        objectVillages.add(new ObjectVillage());
        objectVillages.add(new ObjectVillage());

        Village village = new Village();
        village.setId(villageId);
        objectVillages.forEach(obj -> obj.setVillage(village));

        when(objectVillageRepository.findAll()).thenReturn(objectVillages);

        List<ObjectVillageDTO> resultDTOs = objectVillageService.getObjectVillageByVillageId(villageId);

        assertEquals(objectVillages.size(), resultDTOs.size());
        verify(objectVillageRepository, times(1)).findAll();
        verify(modelMapper, times(objectVillages.size())).map(any(), eq(ObjectVillageDTO.class));
    }


    @Test
    void testGetObjectVillageByVillageIdWhenDatabaseIsEmpty() {
        Long villageId = 1L;

        when(objectVillageRepository.findAll()).thenReturn(Collections.emptyList());

        List<ObjectVillageDTO> resultDTOs = objectVillageService.getObjectVillageByVillageId(villageId);

        assertTrue(resultDTOs.isEmpty());
        verify(objectVillageRepository, times(1)).findAll();
        verify(modelMapper, times(0)).map(any(), eq(ObjectVillageDTO.class));
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

    @Test
    void testGetDistinctObjectVillagesWithDuplicateCombinations() {
        Long villageId = 1L;
        List<ObjectVillage> mockData = new ArrayList<>();
        Village village = new Village();
        village.setId(villageId);
        mockData.add(new ObjectVillage(1L, village, new ObjectAroundVillage(101L, "Object A"), Distance.ON_31_TO_50_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillage(2L, village, new ObjectAroundVillage(102L, "Object B"), Distance.ON_10_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillage(3L, village, new ObjectAroundVillage(101L, "Object A"), Distance.ON_31_TO_50_KM, true, LocalDateTime.now()));

        when(objectVillageRepository.findByVillageIdAndVillageStatus(villageId, true)).thenReturn(mockData);

        List<ObjectVillageDTO> result = objectVillageService.getDistinctObjectVillagesByVillageId(villageId);
        assertEquals(2, result.size());
    }

    @Test
    void testGetDistinctObjectVillagesWithUniqueCombinations() {
        Long villageId = 2L;
        List<ObjectVillage> mockData = new ArrayList<>();
        Village village = new Village();
        village.setId(villageId);
        mockData.add(new ObjectVillage(4L, village, new ObjectAroundVillage(201L, "Object X"), Distance.ON_31_TO_50_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillage(5L, village, new ObjectAroundVillage(202L, "Object Y"), Distance.ON_10_KM, true, LocalDateTime.now()));

        when(objectVillageRepository.findByVillageIdAndVillageStatus(villageId, true)).thenReturn(mockData);

        List<ObjectVillageDTO> result = objectVillageService.getDistinctObjectVillagesByVillageId(villageId);
        assertEquals(2, result.size());
    }

    @Test
    void testGetDistinctObjectVillagesWithNoData() {
        Long villageId = 3L;
        List<ObjectVillage> mockData = new ArrayList<>();

        when(objectVillageRepository.findByVillageId(villageId)).thenReturn(mockData);

        List<ObjectVillageDTO> result = objectVillageService.getDistinctObjectVillagesByVillageId(villageId);
        assertEquals(0, result.size());
    }


    @Test
    void testGetObjectVillageResponsesWithObjectsForAllDistances() {
        List<ObjectVillageDTO> mockData = new ArrayList<>();
        mockData.add(new ObjectVillageDTO(1L, 1L, 101L,  Distance.ON_10_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(2L, 1L, 102L,  Distance.ON_10_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(3L, 1L, 201L,  Distance.ON_11_TO_30KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(4L, 1L, 202L,  Distance.ON_11_TO_30KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(5L, 1L, 301L,  Distance.ON_31_TO_50_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(6L, 1L, 302L,  Distance.ON_31_TO_50_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(7L, 1L, 401L,  Distance.OVER_50_KM, true, LocalDateTime.now()));

        ObjectAroundVillageDTO object1 = new ObjectAroundVillageDTO(101L, "Type A");
        ObjectAroundVillageDTO object2 = new ObjectAroundVillageDTO(102L, "Type B");
        ObjectAroundVillageDTO object3 = new ObjectAroundVillageDTO(201L, "Type X");
        ObjectAroundVillageDTO object4 = new ObjectAroundVillageDTO(202L, "Type Y");
        ObjectAroundVillageDTO object5 = new ObjectAroundVillageDTO(301L, "Type P");
        ObjectAroundVillageDTO object6 = new ObjectAroundVillageDTO(302L, "Type Q");
        ObjectAroundVillageDTO object7 = new ObjectAroundVillageDTO(401L, "Type M");

        when(objectAroundVillageService.getObjectAroundVillageById(101L)).thenReturn(object1);
        when(objectAroundVillageService.getObjectAroundVillageById(102L)).thenReturn(object2);
        when(objectAroundVillageService.getObjectAroundVillageById(201L)).thenReturn(object3);
        when(objectAroundVillageService.getObjectAroundVillageById(202L)).thenReturn(object4);
        when(objectAroundVillageService.getObjectAroundVillageById(301L)).thenReturn(object5);
        when(objectAroundVillageService.getObjectAroundVillageById(302L)).thenReturn(object6);
        when(objectAroundVillageService.getObjectAroundVillageById(401L)).thenReturn(object7);

        List<ObjectVillageResponse> result = objectVillageService.getObjectVillageResponses(mockData);
        assertEquals(3, result.size());

        ObjectVillageResponse response1 = result.get(0);
        assertEquals(Distance.ON_10_KM, response1.getDistance());
        assertEquals("Type A, Type B", response1.getObjects());

        ObjectVillageResponse response2 = result.get(1);
        assertEquals(Distance.ON_11_TO_30KM, response2.getDistance());
        assertEquals("Type X, Type Y", response2.getObjects());

        ObjectVillageResponse response3 = result.get(2);
        assertEquals(Distance.ON_31_TO_50_KM, response3.getDistance());
        assertEquals("Type P, Type Q", response3.getObjects());
    }


    @Test
    void testGetObjectVillageResponsesWithNoData() {
        List<ObjectVillageDTO> mockData = new ArrayList<>();

        List<ObjectVillageResponse> result = objectVillageService.getObjectVillageResponses(mockData);
        assertEquals(0, result.size());
    }

    @Test
    void testGetObjectVillageResponsesWithObjectsForSpecificDistances() {
        List<ObjectVillageDTO> mockData = new ArrayList<>();
        mockData.add(new ObjectVillageDTO(1L, 1L, 101L, Distance.ON_11_TO_30KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(2L, 1L, 102L, Distance.ON_11_TO_30KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(3L, 1L, 201L,  Distance.ON_31_TO_50_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(4L, 1L, 202L,  Distance.ON_31_TO_50_KM, true, LocalDateTime.now()));
        mockData.add(new ObjectVillageDTO(5L, 1L, 301L,  Distance.OVER_50_KM, true, LocalDateTime.now()));

        ObjectAroundVillageDTO object1 = new ObjectAroundVillageDTO(101L, "Type A");
        ObjectAroundVillageDTO object2 = new ObjectAroundVillageDTO(102L, "Type B");
        ObjectAroundVillageDTO object3 = new ObjectAroundVillageDTO(201L, "Type X");
        ObjectAroundVillageDTO object4 = new ObjectAroundVillageDTO(202L, "Type Y");
        ObjectAroundVillageDTO object5 = new ObjectAroundVillageDTO(301L, "Type P");

        when(objectAroundVillageService.getObjectAroundVillageById(101L)).thenReturn(object1);
        when(objectAroundVillageService.getObjectAroundVillageById(102L)).thenReturn(object2);
        when(objectAroundVillageService.getObjectAroundVillageById(201L)).thenReturn(object3);
        when(objectAroundVillageService.getObjectAroundVillageById(202L)).thenReturn(object4);
        when(objectAroundVillageService.getObjectAroundVillageById(301L)).thenReturn(object5);

        List<ObjectVillageResponse> result = objectVillageService.getObjectVillageResponses(mockData);
        assertEquals(2, result.size());

        ObjectVillageResponse response1 = result.get(0);
        assertEquals(Distance.ON_11_TO_30KM, response1.getDistance());
        assertEquals("Type A, Type B", response1.getObjects());

        ObjectVillageResponse response2 = result.get(1);
        assertEquals(Distance.ON_31_TO_50_KM, response2.getDistance());
        assertEquals("Type X, Type Y", response2.getObjects());
    }

}
