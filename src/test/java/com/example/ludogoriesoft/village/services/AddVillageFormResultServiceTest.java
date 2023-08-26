package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.enums.*;

import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddVillageFormResultServiceTest {

    @InjectMocks
    AddVillageFormResultService addVillageFormResultService;
    @Mock
    VillageLivingConditionService villageLivingConditionService;
    @Mock
    VillagePopulationAssertionService villagePopulationAssertionService;
    @Mock
    VillageAnswerQuestionService villageAnswerQuestionService;
    @Mock
    QuestionService questionService;
    @Mock
    ObjectVillageService objectVillageService;
    @Mock
    EthnicityVillageService ethnicityVillageService;
    @Mock
    GroundCategoryService groundCategoryService;
    @Mock
    VillageGroundCategoryService villageGroundCategoryService;
    @Mock
    PopulationService populationService;
    @Mock
    private VillageService villageService;
    @Mock
    private VillageImageService villageImageService;


    @Test
    void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSIsEmpty() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setVillageLivingConditionDTOS(new ArrayList<>());

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verifyNoInteractions(villageLivingConditionService);
    }

    @Test
    void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSHasNullConsents() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillageLivingConditionDTO> dtos = new ArrayList<>();
        dtos.add(new VillageLivingConditionDTO(null, villageId, 1L, null, true, now(), null));

        addVillageFormResult.setVillageLivingConditionDTOS(dtos);

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verifyNoInteractions(villageLivingConditionService);
    }


    @Test
    void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSHasNonNullConsents() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillageLivingConditionDTO> dtos = new ArrayList<>();
        dtos.add(new VillageLivingConditionDTO());
        dtos.add(new VillageLivingConditionDTO(null, villageId, 1L, Consents.COMPLETELY_AGREED, true, now(), null));

        addVillageFormResult.setVillageLivingConditionDTOS(dtos);

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verify(villageLivingConditionService, times(1)).createVillageLivingCondition(any(VillageLivingConditionDTO.class));
    }


    @Test
    void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSIsEmpty() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setVillagePopulationAssertionDTOS(new ArrayList<>());

        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verifyNoInteractions(villagePopulationAssertionService);
    }

    @Test
    void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSHasNullAnswers() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillagePopulationAssertionDTO> dtos = new ArrayList<>();
        dtos.add(new VillagePopulationAssertionDTO(null, villageId, 1L, null, false, now(), null));

        addVillageFormResult.setVillagePopulationAssertionDTOS(dtos);

        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verifyNoInteractions(villagePopulationAssertionService);
    }

    @Test
    void testCreateObjectVillagesFromAddVillageFormResultNoObjectVillageDTOS() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setObjectVillageDTOS(new ArrayList<>()); // Set an empty list instead of null

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verify(objectVillageService, never()).createObjectVillage(any(ObjectVillageDTO.class));
    }


    @Test
    void testCreateObjectVillagesFromAddVillageFormResultEmptyObjectVillageDTOS() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();

        addVillageFormResult.setObjectVillageDTOS(objectVillageDTOS);

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verify(objectVillageService, never()).createObjectVillage(any(ObjectVillageDTO.class));
    }

    @Test
    void testCreateObjectVillagesFromAddVillageFormResultNullDistance() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();
        LocalDateTime localDateTime = TimestampUtils.getCurrentTimestamp();

        ObjectVillageDTO dto1 = new ObjectVillageDTO(null, villageId, 1L, Distance.ON_31_TO_50_KM, false, localDateTime, null);
        ObjectVillageDTO dto2 = new ObjectVillageDTO(null, villageId, 2L, null, false, localDateTime, null);


        objectVillageDTOS.add(new ObjectVillageDTO());
        objectVillageDTOS.add(dto1);
        objectVillageDTOS.add(dto2);

        addVillageFormResult.setObjectVillageDTOS(objectVillageDTOS);

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult, localDateTime);

        verify(objectVillageService, times(1)).createObjectVillage(dto1);
        verify(objectVillageService, never()).createObjectVillage(dto2);
    }

    @Test
    void createVillageAnswerQuestionsFromAddVillageFormResultValidDataCreatesVillageAnswerQuestions() {
        Long villageId = 1L;
        List<String> questionResponses = Arrays.asList("Answer 1", "Answer 2", "Answer 3");
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setQuestionResponses(questionResponses);

        List<QuestionDTO> questionsDTO = Arrays.asList(
                new QuestionDTO(1L, "Question 1"),
                new QuestionDTO(2L, "Question 2"),
                new QuestionDTO(3L, "Question 3")
        );
        Mockito.when(questionService.getAllQuestions()).thenReturn(questionsDTO);

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        ArgumentCaptor<VillageAnswerQuestionDTO> captor = ArgumentCaptor.forClass(VillageAnswerQuestionDTO.class);
        Mockito.verify(villageAnswerQuestionService, Mockito.times(questionResponses.size())).createVillageAnswerQuestion(captor.capture());

        List<VillageAnswerQuestionDTO> capturedDTOs = captor.getAllValues();

        assertEquals(questionResponses.size(), capturedDTOs.size(), "Number of created VillageAnswerQuestionDTOs should match the number of question responses.");

        for (int i = 0; i < questionResponses.size(); i++) {
            VillageAnswerQuestionDTO capturedDTO = capturedDTOs.get(i);
            assertEquals(villageId, capturedDTO.getVillageId(), "Village ID should match.");
            assertEquals(questionsDTO.get(i).getId(), capturedDTO.getQuestionId(), "Question ID should match.");
            assertEquals(questionResponses.get(i), capturedDTO.getAnswer(), "Answer should match.");
        }
    }

    @Test
    void createVillageAnswerQuestionsFromAddVillageFormResultEmptyQuestionResponsesNoVillageAnswerQuestionsCreated() {
        Long villageId = 1L;
        List<String> questionResponses = Collections.emptyList();
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setQuestionResponses(questionResponses);

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        Mockito.verify(villageAnswerQuestionService, Mockito.never()).createVillageAnswerQuestion(Mockito.any(VillageAnswerQuestionDTO.class));
    }

    @Test
    void createVillageAnswerQuestionsFromAddVillageFormResultNullQuestionResponsesNoVillageAnswerQuestionsCreated() {
        Long villageId = 1L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setQuestionResponses(new ArrayList<>());

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        Mockito.verify(villageAnswerQuestionService, Mockito.never()).createVillageAnswerQuestion(Mockito.any(VillageAnswerQuestionDTO.class));
    }


    @Test
    void createEthnicityVillagesFromAddVillageFormResultEmptyEthnicityDTOIdsNoEthnicityVillagesCreated() {
        Long villageId = 1L;
        List<Long> ethnicityDTOIds = Collections.emptyList();
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setEthnicityDTOIds(ethnicityDTOIds);

        addVillageFormResultService.createEthnicityVillagesFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        Mockito.verify(ethnicityVillageService, Mockito.never()).createEthnicityVillage(Mockito.any(EthnicityVillageDTO.class));
    }

    @Test
    void testCreateVillageGroundCategoryWithNewVillage() {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(1L);
        when(groundCategoryService.getByGroundCategoryName(anyString())).thenReturn(groundCategoryDTO);

        when(villageGroundCategoryService.findVillageGroundCategoryDTOByVillageId(anyLong())).thenThrow(new ApiRequestException("Not found"));

        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setGroundCategoryName("Test Ground Category Name");
        addVillageFormResultService.createVillageGroundCategoryFromAddVillageFormResult(1L, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verify(villageGroundCategoryService).createVillageGroundCategoryDTO(any(VillageGroundCategoryDTO.class));
        verify(villageGroundCategoryService, never()).updateVillageGroundCategory(anyLong(), any(VillageGroundCategoryDTO.class));
    }

    @Test
    void testCreateVillageGroundCategoryWithExistingVillage() {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(1L);
        when(groundCategoryService.getByGroundCategoryName(anyString())).thenReturn(groundCategoryDTO);

        VillageGroundCategoryDTO existingVillageGroundCategoryDTO = new VillageGroundCategoryDTO();
        existingVillageGroundCategoryDTO.setId(1L);
        when(villageGroundCategoryService.findVillageGroundCategoryDTOByVillageId(anyLong())).thenReturn(existingVillageGroundCategoryDTO);

        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setGroundCategoryName("Test Ground Category Name");
        addVillageFormResultService.createVillageGroundCategoryFromAddVillageFormResult(1L, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verify(villageGroundCategoryService).updateVillageGroundCategory(anyLong(), any(VillageGroundCategoryDTO.class));
        verify(villageGroundCategoryService, never()).createVillageGroundCategoryDTO(any(VillageGroundCategoryDTO.class));
    }

    @Test
    void testCreatePopulationFromAddVillageFormResult() {
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setPopulationDTO(new PopulationDTO());
        when(populationService.createPopulation(any())).thenReturn(new PopulationDTO());

        addVillageFormResultService.createPopulationFromAddVillageFormResult(1L, addVillageFormResult, LocalDateTime.now());

        verify(populationService).createPopulation(any(PopulationDTO.class));
    }


    //@Test
    //void testGetNumberOfPopulationByAddVillageFormResultWhenLessThanOrEqualTo10() {
    //    AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//
    //    PopulationDTO populationDTO = new PopulationDTO();
    //    populationDTO.setPopulationCount(10);
    //    addVillageFormResult.setPopulationDTO(populationDTO);
//
    //    NumberOfPopulation expected = NumberOfPopulation.UP_TO_10_PEOPLE;
    //    NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(addVillageFormResult);
//
    //    assertEquals(expected, actual);
    //}
//
    //@Test
    //void testGetNumberOfPopulationByAddVillageFormResultWhenLessThanOrEqualTo50() {
    //    AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//
    //    PopulationDTO populationDTO = new PopulationDTO();
    //    populationDTO.setPopulationCount(50);
    //    addVillageFormResult.setPopulationDTO(populationDTO);
//
    //    NumberOfPopulation expected = NumberOfPopulation.FROM_11_TO_50_PEOPLE;
    //    NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(addVillageFormResult);
//
    //    assertEquals(expected, actual);
    //}
//
//
    //@Test
    //void testGetNumberOfPopulationByAddVillageFormResultWhenLessThanOrEqualTo200() {
    //    AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
    //    PopulationDTO populationDTO = new PopulationDTO();
    //    populationDTO.setPopulationCount(200);
    //    addVillageFormResult.setPopulationDTO(populationDTO);
//
    //    NumberOfPopulation expected = NumberOfPopulation.FROM_51_TO_200_PEOPLE;
    //    NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(addVillageFormResult);
//
    //    assertEquals(expected, actual);
    //}
//
//
    //@Test
    //void testGetNumberOfPopulationByAddVillageFormResultWhenLessThanOrEqualTo500() {
    //    AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//
    //    PopulationDTO populationDTO = new PopulationDTO();
    //    populationDTO.setPopulationCount(500);
    //    addVillageFormResult.setPopulationDTO(populationDTO);
//
    //    NumberOfPopulation expected = NumberOfPopulation.FROM_201_TO_500_PEOPLE;
    //    NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(addVillageFormResult);
//
    //    assertEquals(expected, actual);
    //}
//
    //@Test
    //void testGetNumberOfPopulationByAddVillageFormResultWhenLessThanOrEqualTo1000() {
    //    AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
    //    PopulationDTO populationDTO = new PopulationDTO();
    //    populationDTO.setPopulationCount(1000);
    //    addVillageFormResult.setPopulationDTO(populationDTO);
//
    //    NumberOfPopulation expected = NumberOfPopulation.FROM_501_TO_1000_PEOPLE;
    //    NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(addVillageFormResult);
//
    //    assertEquals(expected, actual);
    //}
//
    //@Test
    //void testGetNumberOfPopulationByAddVillageFormResultWhenLessThanOrEqualTo2000() {
    //    AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
    //    PopulationDTO populationDTO = new PopulationDTO();
    //    populationDTO.setPopulationCount(2000);
    //    addVillageFormResult.setPopulationDTO(populationDTO);
//
    //    NumberOfPopulation expected = NumberOfPopulation.FROM_1001_TO_2000_PEOPLE;
    //    NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(addVillageFormResult);
//
    //    assertEquals(expected, actual);
    //}
//
    //@Test
    //void testGetNumberOfPopulationByAddVillageFormResultWhenMoreThan2000() {
    //    AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//
    //    PopulationDTO populationDTO = new PopulationDTO();
    //    populationDTO.setPopulationCount(3000);
    //    addVillageFormResult.setPopulationDTO(populationDTO);
//
    //    NumberOfPopulation expected = NumberOfPopulation.FROM_2000_PEOPLE;
    //    NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(addVillageFormResult);
//
    //    assertEquals(expected, actual);
    //}


    @Test
    void testCreateVillageAnswerQuestionsFromAddVillageFormResultDifferentSizes() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setQuestionResponses(Arrays.asList("Answer 1", "Answer 2"));

        List<QuestionDTO> questionsDTO = Arrays.asList(
                new QuestionDTO(1L, "Question 1"),
                new QuestionDTO(2L, "Question 2"),
                new QuestionDTO(3L, "Question 3")
        );
        when(questionService.getAllQuestions()).thenReturn(questionsDTO);

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult, TimestampUtils.getCurrentTimestamp());

        verify(villageAnswerQuestionService, times(2)).createVillageAnswerQuestion(any(VillageAnswerQuestionDTO.class));
    }


    @Test
    void testCreatePopulationFromAddVillageFormResult1() {
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setPopulationCount(100);
        populationDTO.setResidents(Residents.FROM_2_TO_5_PERCENT);
        populationDTO.setChildren(Children.FROM_11_TO_20);
        populationDTO.setForeigners(Foreigners.NO);

        addVillageFormResult.setPopulationDTO(populationDTO);

        Long villageId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();

        when(populationService.createPopulation(any(PopulationDTO.class))).thenReturn(populationDTO);

        addVillageFormResultService.createPopulationFromAddVillageFormResult(villageId, addVillageFormResult, localDateTime);

        verify(populationService, times(1)).createPopulation(populationDTO);
    }



//    @Test
//    void testCreateAddVillageFormResult() {
//        VillageDTO savedVillage = new VillageDTO();
//        PopulationDTO savedPopulation = new PopulationDTO();
//
//        VillageDTO villageDTO = new VillageDTO();
//        villageDTO.setRegion("Region");
//
//        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
//        groundCategoryDTO.setId(1L);
//
//
//        PopulationDTO populationDTO = new PopulationDTO();
//        populationDTO.setVillageId(savedVillage.getId());
//
//        when(populationService.createPopulation(any(PopulationDTO.class))).thenReturn(savedPopulation);
//
//        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//        addVillageFormResult.setVillageDTO(villageDTO);
//        addVillageFormResult.setPopulationDTO(populationDTO);
//
//
//
//        addVillageFormResult.setImageBytes(Collections.singletonList(new byte[0]));
//        addVillageFormResult.setGroundCategoryName("Ground Category Name");
//        addVillageFormResult.setEthnicityDTOIds(new ArrayList<>());
//        addVillageFormResult.setQuestionResponses(new ArrayList<>());
//        addVillageFormResult.setObjectVillageDTOS(new ArrayList<>());
//        addVillageFormResult.setVillagePopulationAssertionDTOS(new ArrayList<>());
//        addVillageFormResult.setVillageLivingConditionDTOS(new ArrayList<>());
//
//
//        when(villageService.createVillage(any(VillageDTO.class))).thenReturn(savedVillage);
//        when(villageGroundCategoryService.updateVillageGroundCategory(anyLong(), any(VillageGroundCategoryDTO.class)))
//                .thenReturn(new VillageGroundCategoryDTO());
//        when(ethnicityVillageService.createEthnicityVillage(any(EthnicityVillageDTO.class)))
//                .thenReturn(new EthnicityVillageDTO());
//        when(questionService.getAllQuestions()).thenReturn(new ArrayList<>());
//        when(villageAnswerQuestionService.createVillageAnswerQuestion(any(VillageAnswerQuestionDTO.class)))
//                .thenReturn(new VillageAnswerQuestionDTO());
//        when(objectVillageService.createObjectVillage(any(ObjectVillageDTO.class)))
//                .thenReturn(new ObjectVillageDTO());
//        when(villagePopulationAssertionService.createVillagePopulationAssertionDTO(any(VillagePopulationAssertionDTO.class)))
//                .thenReturn(new VillagePopulationAssertionDTO());
//        when(villageLivingConditionService.createVillageLivingCondition(any(VillageLivingConditionDTO.class)))
//                .thenReturn(new VillageLivingConditionDTO());
//        when(villageImageService.createImagePaths(eq(addVillageFormResult.getImageBytes()), anyLong(), any(LocalDateTime.class), eq(false)))
//                .thenReturn(new ArrayList<>());
//
//        AddVillageFormResult result = addVillageFormResultService.create(addVillageFormResult);
//
//        verify(villageService).createVillage(any(VillageDTO.class));
//        verify(populationService).createPopulation(any(PopulationDTO.class));
//
//        assertEquals(addVillageFormResult, result);
//
//    }




}
