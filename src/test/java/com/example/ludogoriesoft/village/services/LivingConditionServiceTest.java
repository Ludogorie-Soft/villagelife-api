package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.LandscapeDTO;
import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.dtos.response.LivingConditionResponse;
import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.enums.Distance;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.LivingConditionRepository;
import com.example.ludogorieSoft.village.repositories.ObjectVillageRepository;
import com.example.ludogorieSoft.village.repositories.VillageLivingConditionRepository;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import org.junit.jupiter.api.Assertions;
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

class LivingConditionServiceTest {
    @Mock
    private LivingConditionRepository livingConditionRepository;

    @InjectMocks
    private LivingConditionService livingConditionService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageLivingConditionRepository villageLivingConditionRepository;
    @Mock
    private ObjectAroundVillageService objectAroundVillageService;
    @Mock
    private ObjectVillageRepository objectVillageRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLivingConditions() {
        LivingCondition livingCondition1 = new LivingCondition(1L, "Condition 1");
        LivingCondition livingCondition2 = new LivingCondition(2L, "Condition 2");

        List<LivingCondition> livingConditions = new ArrayList<>();
        livingConditions.add(livingCondition1);
        livingConditions.add(livingCondition2);

        LivingConditionDTO livingConditionDTO1 = new LivingConditionDTO(1L, "Condition 1");
        LivingConditionDTO livingConditionDTO2 = new LivingConditionDTO(2L, "Condition 2");

        List<LivingConditionDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(livingConditionDTO1);
        expectedDTOs.add(livingConditionDTO2);

        when(livingConditionRepository.findAll()).thenReturn(livingConditions);
        when(modelMapper.map(livingCondition1, LivingConditionDTO.class)).thenReturn(livingConditionDTO1);
        when(modelMapper.map(livingCondition2, LivingConditionDTO.class)).thenReturn(livingConditionDTO2);

        List<LivingConditionDTO> result = livingConditionService.getAllLivingConditions();

        verify(livingConditionRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(livingCondition1, LivingConditionDTO.class);
        verify(modelMapper, times(1)).map(livingCondition2, LivingConditionDTO.class);
        Assertions.assertEquals(expectedDTOs, result);
    }

    @Test
    void testGetAllLivingConditionsWhenNoLivingConditionsExist() {
        List<LivingCondition> livingConditions = new ArrayList<>();
        List<LivingConditionDTO> expectedDTOs = new ArrayList<>();

        when(livingConditionRepository.findAll()).thenReturn(livingConditions);

        List<LivingConditionDTO> result = livingConditionService.getAllLivingConditions();

        verify(livingConditionRepository, times(1)).findAll();
        Assertions.assertEquals(expectedDTOs, result);
    }

    @Test
    void testGetLivingConditionByIdWithExistingId() {
        Long livingConditionId = 1L;
        LivingCondition livingCondition = new LivingCondition(livingConditionId, "Condition");
        LivingConditionDTO expectedDTO = new LivingConditionDTO(livingConditionId, "Condition");

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(livingCondition));
        when(modelMapper.map(livingCondition, LivingConditionDTO.class)).thenReturn(expectedDTO);

        LivingConditionDTO result = livingConditionService.getLivingConditionById(livingConditionId);

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        Assertions.assertEquals(expectedDTO, result);
    }

    @Test
    void testGetLivingConditionByIdWithNonExistingId() {
        Long livingConditionId = 1L;
        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.getLivingConditionById(livingConditionId));
        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(modelMapper, never()).map(any(Landscape.class), eq(LandscapeDTO.class));
    }

    @Test
    void testCreateLivingCondition() {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Test Condition");
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setLivingConditionName("Test Condition");

        when(livingConditionRepository.existsByLivingConditionName("Test Condition")).thenReturn(false);
        when(modelMapper.map(livingCondition, LivingConditionDTO.class)).thenReturn(livingConditionDTO);
        when(livingConditionRepository.save(any(LivingCondition.class))).thenReturn(livingCondition);

        LivingConditionDTO result = livingConditionService.createLivingCondition(livingConditionDTO);

        assertNotNull(result);
        assertEquals("Test Condition", result.getLivingConditionName());
        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Test Condition");
        verify(livingConditionRepository, times(1)).save(any(LivingCondition.class));
    }

    @Test
    void testCreateLivingConditionExistingCondition() {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Test Condition");

        when(livingConditionRepository.existsByLivingConditionName("Test Condition")).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> livingConditionService.createLivingCondition(livingConditionDTO));

        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Test Condition");
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testUpdateLivingCondition() {
        Long id = 1L;
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Updated Condition");
        LivingCondition existingLivingCondition = new LivingCondition();
        existingLivingCondition.setId(id);
        existingLivingCondition.setLivingConditionName("Old Condition");

        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);

        when(livingConditionRepository.findById(id)).thenReturn(optionalLivingCondition);
        when(livingConditionRepository.existsByLivingConditionName("Updated Condition")).thenReturn(false);
        when(livingConditionRepository.save(any(LivingCondition.class))).thenReturn(existingLivingCondition);
        when(modelMapper.map(existingLivingCondition, LivingConditionDTO.class)).thenReturn(livingConditionDTO);

        LivingConditionDTO result = livingConditionService.updateLivingCondition(id, livingConditionDTO);

        assertNotNull(result);
        assertEquals("Updated Condition", result.getLivingConditionName());
        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Updated Condition");
        verify(livingConditionRepository, times(1)).save(any(LivingCondition.class));
    }

    @Test
    void testUpdateLivingConditionInvalidId() {
        Long id = 1L;
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Updated Condition");

        when(livingConditionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(id, livingConditionDTO));

        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testUpdateLivingConditionExistingCondition() {
        Long id = 1L;
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Updated Condition");
        LivingCondition existingLivingCondition = new LivingCondition();
        existingLivingCondition.setId(id);
        existingLivingCondition.setLivingConditionName("Old Condition");

        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);

        when(livingConditionRepository.findById(id)).thenReturn(optionalLivingCondition);
        when(livingConditionRepository.existsByLivingConditionName("Updated Condition")).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(id, livingConditionDTO));

        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Updated Condition");
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testDeleteLivingConditionWithExistingId() {
        Long livingConditionId = 123L;
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setId(livingConditionId);

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(livingCondition));
        livingConditionService.deleteLivingCondition(livingConditionId);

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, times(1)).delete(livingCondition);
    }

    @Test
    void testDeleteLivingConditionWithNonExistingId() {
        Long livingConditionId = 123L;
        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.deleteLivingCondition(livingConditionId));
        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, never()).delete(any(LivingCondition.class));
    }

    @Test
    void checkLivingConditionShouldReturnExistingLivingCondition() {
        Long livingConditionId = 1L;
        LivingCondition existingLivingCondition = new LivingCondition();
        existingLivingCondition.setId(livingConditionId);
        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(optionalLivingCondition);

        LivingCondition result = livingConditionService.checkLivingCondition(livingConditionId);

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        assertEquals(existingLivingCondition, result);
    }

    @Test
    void checkLivingConditionShouldThrowExceptionWhenLivingConditionNotFound() {
        Long livingConditionId = 1L;
        Optional<LivingCondition> optionalLivingCondition = Optional.empty();

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(optionalLivingCondition);

        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.checkLivingCondition(livingConditionId));

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
    }


    @Test
    void testCreateLivingConditionWithBlankName() {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("");

        assertThrows(ApiRequestException.class, () -> livingConditionService.createLivingCondition(livingConditionDTO));
        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testUpdateLivingConditionWithInvalidData() {
        Long id = 1L;
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("");
        assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(id, livingConditionDTO));
        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }


    @Test
    void testUpdateLivingConditionWithNullData() {
        Long id = 1L;

        assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(id, null));
        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testGetPercentageWithMultipleLivingConditions() {
        LocalDateTime localDateTime = TimestampUtils.getCurrentTimestamp();
        Long villageId = 1L;
        Long conditionId = 10L;
        List<VillageLivingConditions> villageLivingConditions = new ArrayList<>();
        Village village = new Village();
        village.setId(villageId);
        LivingCondition livingCondition = new LivingCondition(conditionId, "Test Condition");

        villageLivingConditions.add(new VillageLivingConditions(1L, village, livingCondition, Consents.COMPLETELY_AGREED, true, localDateTime, null));
        villageLivingConditions.add(new VillageLivingConditions(1L, village, livingCondition, Consents.DISAGREE, true, localDateTime, null));
        villageLivingConditions.add(new VillageLivingConditions(1L, village, livingCondition, Consents.RATHER_DISAGREE, true, localDateTime, null));
        when(villageLivingConditionRepository.findByVillageIdAndLivingConditionIdAndVillageStatus(villageId, conditionId, true)).thenReturn(villageLivingConditions);

        double expectedPercentage = (100 + 20 + 40) / 3.0;
        double result = livingConditionService.getPercentage(villageId, conditionId, true, localDateTime.toString());
        assertEquals(expectedPercentage, result, 0.0001);
    }

    @Test
    void testGetPercentageWithSingleLivingCondition() {
        LocalDateTime localDateTime = TimestampUtils.getCurrentTimestamp();

        Long villageId = 2L;
        Long conditionId = 20L;
        List<VillageLivingConditions> villageLivingConditions = new ArrayList<>();
        Village village = new Village();
        village.setId(villageId);
        LivingCondition livingCondition = new LivingCondition(conditionId, "Test Condition");

        VillageLivingConditions villageLivingCondition1 = new VillageLivingConditions(1L, village, livingCondition, Consents.RATHER_DISAGREE, true, localDateTime, null);
        VillageLivingConditions villageLivingCondition2 = new VillageLivingConditions(2L, village, livingCondition, Consents.CANT_DECIDE, true, localDateTime, null);
        villageLivingConditions.add(villageLivingCondition1);
        villageLivingConditions.add(villageLivingCondition2);

        when(villageLivingConditionRepository.findByVillageIdAndLivingConditionIdAndVillageStatus(villageId, conditionId, true)).thenReturn(villageLivingConditions);
        double expectedPercentage = (40 + 60) / 2.0;
        double result = livingConditionService.getPercentage(villageId, conditionId, true, localDateTime.toString());
        assertEquals(expectedPercentage, result, 0.0001);
    }

    @Test
    void testGetLivingConditionsForumPercentageWhenObjectsFound() {
        List<ObjectAroundVillageDTO> objectAroundVillageDTOS = Arrays.asList(
                new ObjectAroundVillageDTO(1L, "Object 1"),
                new ObjectAroundVillageDTO(2L, "Object 2")
        );
        when(objectAroundVillageService.getAllObjectsAroundVillage()).thenReturn(objectAroundVillageDTOS);

        when(objectVillageRepository.existsByVillageIdAndObjectIdAndVillageStatus(100L, 1L, true)).thenReturn(true);
        when(objectVillageRepository.existsByVillageIdAndObjectIdAndVillageStatus(100L, 2L, true)).thenReturn(true);

        Village village = new Village();
        village.setId(100L);

        ObjectAroundVillage objectAroundVillage1 = new ObjectAroundVillage(1L, "Object 1");
        ObjectAroundVillage objectAroundVillage2 = new ObjectAroundVillage(1L, "Object 2");

        List<ObjectVillage> objectVillages1 = Arrays.asList(
                new ObjectVillage(1L, village, objectAroundVillage1, Distance.IN_THE_VILLAGE, true, TimestampUtils.getCurrentTimestamp(), null),
                new ObjectVillage(2L, village, objectAroundVillage1, Distance.ON_31_TO_50_KM, true, TimestampUtils.getCurrentTimestamp(), null)
        );
        List<ObjectVillage> objectVillages2 = Arrays.asList(
                new ObjectVillage(3L, village, objectAroundVillage2, Distance.ON_10_KM, true, TimestampUtils.getCurrentTimestamp(), null),
                new ObjectVillage(4L, village, objectAroundVillage2, Distance.OVER_50_KM, true, TimestampUtils.getCurrentTimestamp(), null)
        );
        when(objectVillageRepository.findByVillageIdAndObjectIdAndVillageStatus(100L, 1L, true)).thenReturn(objectVillages1);
        when(objectVillageRepository.findByVillageIdAndObjectIdAndVillageStatus(100L, 2L, true)).thenReturn(objectVillages2);

        double result = livingConditionService.getLivingConditionsForumPercentage(100L, true, TimestampUtils.getCurrentTimestamp().toString());

        assertEquals(60, result, 0.01);
    }

    @Test
    void testGetLivingConditionsForumPercentageWhenNoObjectsFound() {
        when(objectAroundVillageService.getAllObjectsAroundVillage()).thenReturn(Collections.emptyList());
        double result = livingConditionService.getLivingConditionsForumPercentage(100L, true, TimestampUtils.getCurrentTimestamp().toString());
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testGetLivingConditionsForumPercentageWhenNoMatchingObjects() {
        List<ObjectAroundVillageDTO> objectAroundVillageDTOS = Arrays.asList(
                new ObjectAroundVillageDTO(1L, "Object 1"),
                new ObjectAroundVillageDTO(2L, "Object 2")
        );
        when(objectAroundVillageService.getAllObjectsAroundVillage()).thenReturn(objectAroundVillageDTOS);

        when(objectVillageRepository.existsByVillageIdAndObjectIdAndVillageStatus(100L, 1L, true)).thenReturn(false);
        when(objectVillageRepository.existsByVillageIdAndObjectIdAndVillageStatus(100L, 2L, true)).thenReturn(false);

        double result = livingConditionService.getLivingConditionsForumPercentage(100L, true, TimestampUtils.getCurrentTimestamp().toString());

        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testGetLivingConditionsMainPercentageWhenNoData() {
        when(villageLivingConditionRepository.existsByVillageIdAndLivingConditionIdAndVillageStatus(eq(200L), anyLong(), eq(true))).thenReturn(false);
        double result = livingConditionService.getLivingConditionsMainPercentage(200L, true, TimestampUtils.getCurrentTimestamp().toString());
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testGetLivingConditionResponsesWhenStatusTrue() {
        Long villageId = 1L;
        boolean status = true;
        String date = "2023-08-19";

        List<String> names = Arrays.asList("Инфраструктура", "Обществен транспорт", "Електрозахранване", "Водоснабдяване", "Мобилен обхват", "Интернет", "ТВ", "Чистота");
        List<Long> livingConditionIds = Arrays.asList(1L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

        when(villageLivingConditionRepository.existsByVillageIdAndLivingConditionIdAndVillageStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(false);

        List<LivingConditionResponse> result = livingConditionService.getLivingConditionResponses(villageId, status, date);

        for (int i = 0; i < names.size(); i++) {
            verify(villageLivingConditionRepository).existsByVillageIdAndLivingConditionIdAndVillageStatus(villageId, livingConditionIds.get(i), status);
        }
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLivingConditionResponsesWhenStatusFalse() {
        Long villageId = 1L;
        boolean status = false;
        String date = "2023-08-19";

        List<String> names = Arrays.asList("Инфраструктура", "Обществен транспорт", "Електрозахранване", "Водоснабдяване", "Мобилен обхват", "Интернет", "ТВ", "Чистота");
        List<Long> livingConditionIds = Arrays.asList(1L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

        when(villageLivingConditionRepository.existsByVillageIdAndLivingConditionIdAndVillageStatusAndDate(anyLong(), anyLong(), anyBoolean(), anyString()))
                .thenReturn(false);

        List<LivingConditionResponse> result = livingConditionService.getLivingConditionResponses(villageId, status, date);

        for (int i = 0; i < names.size(); i++) {
            verify(villageLivingConditionRepository).existsByVillageIdAndLivingConditionIdAndVillageStatusAndDate(villageId, livingConditionIds.get(i), false, date);
        }

        assertTrue(result.isEmpty());
    }
}