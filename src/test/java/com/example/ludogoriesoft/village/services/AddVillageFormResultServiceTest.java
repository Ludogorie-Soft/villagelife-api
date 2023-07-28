package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.enums.*;

import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Population;

import org.junit.jupiter.api.Assertions;
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

    @Test
    void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSIsEmpty() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setVillageLivingConditionDTOS(new ArrayList<>());

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult);

        verifyNoInteractions(villageLivingConditionService);
    }

    @Test
    void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSHasNullConsents() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillageLivingConditionDTO> dtos = new ArrayList<>();
<<<<<<< HEAD
        dtos.add(new VillageLivingConditionDTO(null, villageId, 1L, null,true,now()));
=======
        dtos.add(new VillageLivingConditionDTO(null, villageId, 1L, null,false,null));
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
        addVillageFormResult.setVillageLivingConditionDTOS(dtos);

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult);

        verifyNoInteractions(villageLivingConditionService);
    }


    @Test
    void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSHasNonNullConsents() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillageLivingConditionDTO> dtos = new ArrayList<>();
        dtos.add(new VillageLivingConditionDTO());
<<<<<<< HEAD
        dtos.add(new VillageLivingConditionDTO(null, villageId, 1L, Consents.COMPLETELY_AGREED,true,now()));
=======
        dtos.add(new VillageLivingConditionDTO(null, villageId, 1L, Consents.COMPLETELY_AGREED,false,null));
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
        addVillageFormResult.setVillageLivingConditionDTOS(dtos);

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(villageLivingConditionService, times(1)).createVillageLivingCondition(any(VillageLivingConditionDTO.class));
    }

//    @Test
//    void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSHasMixedConsents() {
//        Long villageId = 12345L;
//        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//        List<VillageLivingConditionDTO> dtos = new ArrayList<>();
//
<<<<<<< HEAD
//        VillageLivingConditionDTO dto1 = new VillageLivingConditionDTO(null, villageId, 1L, Consents.COMPLETELY_AGREED,false,now());
//        VillageLivingConditionDTO dto2 = new VillageLivingConditionDTO(null, villageId, 2L, Consents.DISAGREE,false,now());
//        VillageLivingConditionDTO dto3 = new VillageLivingConditionDTO(null, villageId, 3L, Consents.CANT_DECIDE,false,now());
=======
//        VillageLivingConditionDTO dto1 = new VillageLivingConditionDTO(null, villageId, 1L, Consents.COMPLETELY_AGREED,false,null);
//        VillageLivingConditionDTO dto2 = new VillageLivingConditionDTO(null, villageId, 2L, Consents.DISAGREE,false,null);
//        VillageLivingConditionDTO dto3 = new VillageLivingConditionDTO(null, villageId, 3L, Consents.CANT_DECIDE,false,null);
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
//
//        dtos.add(new VillageLivingConditionDTO());
//        dtos.add(dto1);
//        dtos.add(dto2);
//        dtos.add(dto3);
//
//        addVillageFormResult.setVillageLivingConditionDTOS(dtos);
//
//        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult);
//
//        verify(villageLivingConditionService, times(3)).createVillageLivingCondition(any(VillageLivingConditionDTO.class));
//        verify(villageLivingConditionService).createVillageLivingCondition(dto1);
//        verify(villageLivingConditionService).createVillageLivingCondition(dto2);
//        verify(villageLivingConditionService).createVillageLivingCondition(dto3);
//    }


    @Test
    void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSIsEmpty() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setVillagePopulationAssertionDTOS(new ArrayList<>());

        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult);

        verifyNoInteractions(villagePopulationAssertionService);
    }

    @Test
    void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSHasNullAnswers() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillagePopulationAssertionDTO> dtos = new ArrayList<>();
<<<<<<< HEAD
        dtos.add(new VillagePopulationAssertionDTO(null, villageId, 1L, null,false,now()));
=======
        dtos.add(new VillagePopulationAssertionDTO(null, villageId, 1L, null,false,null));
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
        addVillageFormResult.setVillagePopulationAssertionDTOS(dtos);

        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult);

        verifyNoInteractions(villagePopulationAssertionService);
    }

//    @Test
//    void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSHasNonNullAnswers() {
//        Long villageId = 12345L;
//        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//        List<VillagePopulationAssertionDTO> dtos = new ArrayList<>();
//
<<<<<<< HEAD
//        VillagePopulationAssertionDTO dto1 = new VillagePopulationAssertionDTO(null, villageId, 1L, Consents.DISAGREE,false,now());
//        VillagePopulationAssertionDTO dto2 = new VillagePopulationAssertionDTO(null, villageId, 2L, Consents.RATHER_AGREE,false,now());
=======
//        VillagePopulationAssertionDTO dto1 = new VillagePopulationAssertionDTO(null, villageId, 1L, Consents.DISAGREE,false,null);
//        VillagePopulationAssertionDTO dto2 = new VillagePopulationAssertionDTO(null, villageId, 2L, Consents.RATHER_AGREE,false,null);
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
//
//        dtos.add(new VillagePopulationAssertionDTO());
//        dtos.add(dto1);
//        dtos.add(dto2);
//
//        addVillageFormResult.setVillagePopulationAssertionDTOS(dtos);
//
//        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult);
//
//        verify(villagePopulationAssertionService, times(2)).createVillagePopulationAssertionDTO(any(VillagePopulationAssertionDTO.class));
//        verify(villagePopulationAssertionService).createVillagePopulationAssertionDTO(dto1);
//        verify(villagePopulationAssertionService).createVillagePopulationAssertionDTO(dto2);
//    }

//    @Test
//    void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSHasMixedAnswers() {
//        Long villageId = 12345L;
//        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//        List<VillagePopulationAssertionDTO> dtos = new ArrayList<>();
//
<<<<<<< HEAD
//        VillagePopulationAssertionDTO dto1 = new VillagePopulationAssertionDTO(null, villageId, 1L, Consents.CANT_DECIDE,false,now());
//        VillagePopulationAssertionDTO dto2 = new VillagePopulationAssertionDTO(null, villageId, 2L, null,false,now());
//        VillagePopulationAssertionDTO dto3 = new VillagePopulationAssertionDTO(null, villageId, 3L, Consents.COMPLETELY_AGREED,false,now());
=======
//        VillagePopulationAssertionDTO dto1 = new VillagePopulationAssertionDTO(null, villageId, 1L, Consents.CANT_DECIDE,false,null);
//        VillagePopulationAssertionDTO dto2 = new VillagePopulationAssertionDTO(null, villageId, 2L, null,false,null);
//        VillagePopulationAssertionDTO dto3 = new VillagePopulationAssertionDTO(null, villageId, 3L, Consents.COMPLETELY_AGREED,false,null);
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
//
//        dtos.add(new VillagePopulationAssertionDTO());
//        dtos.add(dto1);
//        dtos.add(dto2);
//        dtos.add(dto3);
//
//        addVillageFormResult.setVillagePopulationAssertionDTOS(dtos);
//
//        when(villagePopulationAssertionService.existsByVillageIdAndPopulatedAssertionIdAndAnswer(villageId, dto1.getPopulatedAssertionId(), dto1.getAnswer())).thenReturn(false);
//        when(villagePopulationAssertionService.existsByVillageIdAndPopulatedAssertionIdAndAnswer(villageId, dto3.getPopulatedAssertionId(), dto3.getAnswer())).thenReturn(false);
//
//        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult);
//
//        verify(villagePopulationAssertionService, times(1)).existsByVillageIdAndPopulatedAssertionIdAndAnswer(villageId, dto1.getPopulatedAssertionId(), dto1.getAnswer());
//        verify(villagePopulationAssertionService, times(1)).existsByVillageIdAndPopulatedAssertionIdAndAnswer(villageId, dto3.getPopulatedAssertionId(), dto3.getAnswer());
//
//        verify(villagePopulationAssertionService, times(1)).createVillagePopulationAssertionDTO(dto1);
//        verify(villagePopulationAssertionService, times(1)).createVillagePopulationAssertionDTO(dto3);
//
//        verifyNoMoreInteractions(villagePopulationAssertionService);
//    }


//    @Test
//    void testCreateObjectVillagesFromAddVillageFormResult() {
//        Long villageId = 12345L;
//        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();
//
<<<<<<< HEAD
//        ObjectVillageDTO dto1 = new ObjectVillageDTO(null, villageId, 1L, Distance.IN_THE_VILLAGE,false,now());
//        ObjectVillageDTO dto2 = new ObjectVillageDTO(null, villageId, 2L, Distance.ON_10_KM,false,now());
//        ObjectVillageDTO dto3 = new ObjectVillageDTO(null, villageId, 3L, null,false,now());
=======
//        ObjectVillageDTO dto1 = new ObjectVillageDTO(null, villageId, 1L, Distance.IN_THE_VILLAGE,false,null);
//        ObjectVillageDTO dto2 = new ObjectVillageDTO(null, villageId, 2L, Distance.ON_10_KM,false,null);
//        ObjectVillageDTO dto3 = new ObjectVillageDTO(null, villageId, 3L, null,false,null);
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
//
//        objectVillageDTOS.add(new ObjectVillageDTO());
//        objectVillageDTOS.add(dto1);
//        objectVillageDTOS.add(dto2);
//        objectVillageDTOS.add(dto3);
//
//        addVillageFormResult.setObjectVillageDTOS(objectVillageDTOS);
//
//        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult);
//
//        verify(objectVillageService, times(2)).createObjectVillage(any(ObjectVillageDTO.class));
//        verify(objectVillageService).createObjectVillage(dto1);
//        verify(objectVillageService).createObjectVillage(dto2);
//        verify(objectVillageService, never()).createObjectVillage(dto3);
//    }

    @Test
    void testCreateObjectVillagesFromAddVillageFormResultNoObjectVillageDTOS() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setObjectVillageDTOS(new ArrayList<>()); // Set an empty list instead of null

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(objectVillageService, never()).createObjectVillage(any(ObjectVillageDTO.class));
    }


    @Test
    void testCreateObjectVillagesFromAddVillageFormResultEmptyObjectVillageDTOS() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();

        addVillageFormResult.setObjectVillageDTOS(objectVillageDTOS);

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(objectVillageService, never()).createObjectVillage(any(ObjectVillageDTO.class));
    }

//    @Test
//    void testCreateObjectVillagesFromAddVillageFormResultNullDistance() {
//        Long villageId = 12345L;
//        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();
//
<<<<<<< HEAD
//        ObjectVillageDTO dto1 = new ObjectVillageDTO(null, villageId, 1L, Distance.ON_31_TO_50_KM,false,now());
//        ObjectVillageDTO dto2 = new ObjectVillageDTO(null, villageId, 2L, null,false,now());
=======
//        ObjectVillageDTO dto1 = new ObjectVillageDTO(null, villageId, 1L, Distance.ON_31_TO_50_KM,false,null);
//        ObjectVillageDTO dto2 = new ObjectVillageDTO(null, villageId, 2L, null,false,null);
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
//
//        objectVillageDTOS.add(new ObjectVillageDTO());
//        objectVillageDTOS.add(dto1);
//        objectVillageDTOS.add(dto2);
//
//        addVillageFormResult.setObjectVillageDTOS(objectVillageDTOS);
//
//        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult);
//
//        verify(objectVillageService, times(1)).createObjectVillage(dto1);
//        verify(objectVillageService, never()).createObjectVillage(dto2);
//    }

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

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult);

        ArgumentCaptor<VillageAnswerQuestionDTO> captor = ArgumentCaptor.forClass(VillageAnswerQuestionDTO.class);
        Mockito.verify(villageAnswerQuestionService, Mockito.times(questionResponses.size())).createVillageAnswerQuestion(captor.capture());

        List<VillageAnswerQuestionDTO> capturedDTOs = captor.getAllValues();

        Assertions.assertEquals(questionResponses.size(), capturedDTOs.size(), "Number of created VillageAnswerQuestionDTOs should match the number of question responses.");

        for (int i = 0; i < questionResponses.size(); i++) {
            VillageAnswerQuestionDTO capturedDTO = capturedDTOs.get(i);
            Assertions.assertEquals(villageId, capturedDTO.getVillageId(), "Village ID should match.");
            Assertions.assertEquals(questionsDTO.get(i).getId(), capturedDTO.getQuestionId(), "Question ID should match.");
            Assertions.assertEquals(questionResponses.get(i), capturedDTO.getAnswer(), "Answer should match.");
        }
    }

    @Test
    void createVillageAnswerQuestionsFromAddVillageFormResultEmptyQuestionResponsesNoVillageAnswerQuestionsCreated() {
        Long villageId = 1L;
        List<String> questionResponses = Collections.emptyList();
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setQuestionResponses(questionResponses);

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult);

        Mockito.verify(villageAnswerQuestionService, Mockito.never()).createVillageAnswerQuestion(Mockito.any(VillageAnswerQuestionDTO.class));
    }

    @Test
    void createVillageAnswerQuestionsFromAddVillageFormResultNullQuestionResponsesNoVillageAnswerQuestionsCreated() {
        Long villageId = 1L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setQuestionResponses(new ArrayList<>());

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult);

        Mockito.verify(villageAnswerQuestionService, Mockito.never()).createVillageAnswerQuestion(Mockito.any(VillageAnswerQuestionDTO.class));
    }

//    @Test
//    void createEthnicityVillagesFromAddVillageFormResultValidDataEthnicityVillagesCreated() {
//        Long villageId = 1L;
//        List<Long> ethnicityDTOIds = Arrays.asList(1L, 2L, 3L);
//        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//        addVillageFormResult.setEthnicityDTOIds(ethnicityDTOIds);
//
//        addVillageFormResultService.createEthnicityVillagesFromAddVillageFormResult(villageId, addVillageFormResult);
//
//        for (Long id : ethnicityDTOIds) {
<<<<<<< HEAD
//            EthnicityVillageDTO expectedDTO = new EthnicityVillageDTO(null, villageId, id,false,now());
//            Mockito.verify(ethnicityVillageService).createEthnicityVillage(expectedDTO);
//        }
//    }

=======
//            EthnicityVillageDTO expectedDTO = new EthnicityVillageDTO(null, villageId, id,false,null);
//            Mockito.verify(ethnicityVillageService).createEthnicityVillage(expectedDTO);
//        }
//    }
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
    @Test
    void createEthnicityVillagesFromAddVillageFormResultEmptyEthnicityDTOIdsNoEthnicityVillagesCreated() {
        Long villageId = 1L;
        List<Long> ethnicityDTOIds = Collections.emptyList();
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setEthnicityDTOIds(ethnicityDTOIds);

        addVillageFormResultService.createEthnicityVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        Mockito.verify(ethnicityVillageService, Mockito.never()).createEthnicityVillage(Mockito.any(EthnicityVillageDTO.class));
    }

//    @Test
//    void createEthnicityVillagesFromAddVillageFormResultLargeNumberOfEthnicityDTOIdsEthnicityVillagesCreated() {
//        Long villageId = 1L;
//        List<Long> ethnicityDTOIds = new ArrayList<>();
//        for (long i = 1; i <= 100; i++) {
//            ethnicityDTOIds.add(i);
//        }
//        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
//        addVillageFormResult.setEthnicityDTOIds(ethnicityDTOIds);
//
//        addVillageFormResultService.createEthnicityVillagesFromAddVillageFormResult(villageId, addVillageFormResult);
//
//        for (Long id : ethnicityDTOIds) {
//            EthnicityVillageDTO expectedDTO = new EthnicityVillageDTO(null, villageId, id,false,now());
//            Mockito.verify(ethnicityVillageService).createEthnicityVillage(expectedDTO);
//        }
//    }
<<<<<<< HEAD

>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)
    @Test
    void testCreateVillageGroundCategoryWithNewVillage() {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(1L);
        when(groundCategoryService.getByGroundCategoryName(anyString())).thenReturn(groundCategoryDTO);

        when(villageGroundCategoryService.findVillageGroundCategoryDTOByVillageId(anyLong())).thenThrow(new ApiRequestException("Not found"));

        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setGroundCategoryName("Test Ground Category Name");
        addVillageFormResultService.createVillageGroundCategoryFromAddVillageFormResult(1L, addVillageFormResult);

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
        addVillageFormResultService.createVillageGroundCategoryFromAddVillageFormResult(1L, addVillageFormResult);

        verify(villageGroundCategoryService).updateVillageGroundCategory(anyLong(), any(VillageGroundCategoryDTO.class));
        verify(villageGroundCategoryService, never()).createVillageGroundCategoryDTO(any(VillageGroundCategoryDTO.class));
    }

    @Test
    void testGetNumberOfPopulationByAddVillageFormResultWhenLessThanOrEqualTo10() {
        AddVillageFormResult result = new AddVillageFormResult();
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setPopulationCount(10);
        result.setVillageDTO(villageDTO);

        NumberOfPopulation expected = NumberOfPopulation.UP_TO_10_PEOPLE;
        NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(result);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetNumberOfPopulationByAddVillageFormResultWhenBetween11And50() {
        AddVillageFormResult result = new AddVillageFormResult();
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setPopulationCount(35);
        result.setVillageDTO(villageDTO);

        NumberOfPopulation expected = NumberOfPopulation.FROM_11_TO_50_PEOPLE;
        NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(result);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    void testGetNumberOfPopulationByAddVillageFormResultWhenBetween51And200() {
        AddVillageFormResult result = new AddVillageFormResult();
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setPopulationCount(125);
        result.setVillageDTO(villageDTO);

        NumberOfPopulation expected = NumberOfPopulation.FROM_51_TO_200_PEOPLE;
        NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(result);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetNumberOfPopulationByAddVillageFormResultWhenBetween201And500() {
        AddVillageFormResult result = new AddVillageFormResult();
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setPopulationCount(450);
        result.setVillageDTO(villageDTO);

        NumberOfPopulation expected = NumberOfPopulation.FROM_201_TO_500_PEOPLE;
        NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(result);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetNumberOfPopulationByAddVillageFormResultWhenBetween501And1000() {
        AddVillageFormResult result = new AddVillageFormResult();
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setPopulationCount(800);
        result.setVillageDTO(villageDTO);

        NumberOfPopulation expected = NumberOfPopulation.FROM_501_TO_1000_PEOPLE;
        NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(result);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetNumberOfPopulationByAddVillageFormResultWhenBetween1001And2000() {
        AddVillageFormResult result = new AddVillageFormResult();
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setPopulationCount(1500);
        result.setVillageDTO(villageDTO);

        NumberOfPopulation expected = NumberOfPopulation.FROM_1001_TO_2000_PEOPLE;
        NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(result);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetNumberOfPopulationByAddVillageFormResultWhenGreaterThan2000() {
        AddVillageFormResult result = new AddVillageFormResult();
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setPopulationCount(3000);
        result.setVillageDTO(villageDTO);

        NumberOfPopulation expected = NumberOfPopulation.FROM_2000_PEOPLE;
        NumberOfPopulation actual = addVillageFormResultService.getNumberOfPopulationByAddVillageFormResult(result);

        Assertions.assertEquals(expected, actual);
    }



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

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(villageAnswerQuestionService, times(2)).createVillageAnswerQuestion(any(VillageAnswerQuestionDTO.class));
    }






    @Test
    void testCreatePopulationFromAddVillageFormResultWithExistingPopulation() {
        String villageName = "Sample Village";
        String regionName = "Sample Region";
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName(villageName);
        villageDTO.setRegion(regionName);
        addVillageFormResult.setVillageDTO(villageDTO);

        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        populationDTO.setResidents(Residents.FROM_21_TO_30_PERCENT);
        populationDTO.setChildren(Children.BELOW_10);
        populationDTO.setForeigners(Foreigners.I_DONT_KNOW);
        addVillageFormResult.setPopulationDTO(populationDTO);

        Population existingPopulation = new Population();
        existingPopulation.setId(1L);
        existingPopulation.setNumberOfPopulation(NumberOfPopulation.FROM_51_TO_200_PEOPLE);
        existingPopulation.setResidents(Residents.FROM_11_TO_20_PERCENT);
        existingPopulation.setChildren(Children.FROM_11_TO_20);
        existingPopulation.setForeigners(Foreigners.YES);

        when(populationService.findPopulationByVillageNameAndRegion(villageName, regionName)).thenReturn(existingPopulation);

        PopulationDTO updatedPopulation = new PopulationDTO();
        updatedPopulation.setId(existingPopulation.getId());
        updatedPopulation.setNumberOfPopulation(populationDTO.getNumberOfPopulation());
        updatedPopulation.setResidents(populationDTO.getResidents());
        updatedPopulation.setChildren(populationDTO.getChildren());
        updatedPopulation.setForeigners(populationDTO.getForeigners());

        when(populationService.updatePopulation(existingPopulation.getId(), populationDTO)).thenReturn(updatedPopulation);

        PopulationDTO resultDTO = addVillageFormResultService.createPopulationFromAddVillageFormResult(addVillageFormResult);

        verify(populationService, times(1)).findPopulationByVillageNameAndRegion(villageName, regionName);
        verify(populationService, times(1)).updatePopulation(existingPopulation.getId(), populationDTO);

        Assertions.assertEquals(updatedPopulation.getId(), resultDTO.getId());
        Assertions.assertEquals(updatedPopulation.getNumberOfPopulation(), resultDTO.getNumberOfPopulation());
        Assertions.assertEquals(updatedPopulation.getResidents(), resultDTO.getResidents());
        Assertions.assertEquals(updatedPopulation.getChildren(), resultDTO.getChildren());
        Assertions.assertEquals(updatedPopulation.getForeigners(), resultDTO.getForeigners());
    }

    @Test
    void testCreatePopulationFromAddVillageFormResultWithNewPopulation() {
        String villageName = "Sample Village";
        String regionName = "Sample Region";
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName(villageName);
        villageDTO.setRegion(regionName);
        addVillageFormResult.setVillageDTO(villageDTO);

        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        populationDTO.setResidents(Residents.FROM_21_TO_30_PERCENT);
        populationDTO.setChildren(Children.BELOW_10);
        populationDTO.setForeigners(Foreigners.I_DONT_KNOW);
        addVillageFormResult.setPopulationDTO(populationDTO);

        when(populationService.findPopulationByVillageNameAndRegion(villageName, regionName)).thenReturn(null);

        PopulationDTO createdPopulation = new PopulationDTO();
        createdPopulation.setId(1L);
        createdPopulation.setNumberOfPopulation(populationDTO.getNumberOfPopulation());
        createdPopulation.setResidents(populationDTO.getResidents());
        createdPopulation.setChildren(populationDTO.getChildren());
        createdPopulation.setForeigners(populationDTO.getForeigners());

        when(populationService.createPopulation(populationDTO)).thenReturn(createdPopulation);

        PopulationDTO resultDTO = addVillageFormResultService.createPopulationFromAddVillageFormResult(addVillageFormResult);

        verify(populationService, times(1)).findPopulationByVillageNameAndRegion(villageName, regionName);
        verify(populationService, times(1)).createPopulation(populationDTO);

        Assertions.assertEquals(createdPopulation.getId(), resultDTO.getId());
        Assertions.assertEquals(createdPopulation.getNumberOfPopulation(), resultDTO.getNumberOfPopulation());
        Assertions.assertEquals(createdPopulation.getResidents(), resultDTO.getResidents());
        Assertions.assertEquals(createdPopulation.getChildren(), resultDTO.getChildren());
        Assertions.assertEquals(createdPopulation.getForeigners(), resultDTO.getForeigners());
    }

}
