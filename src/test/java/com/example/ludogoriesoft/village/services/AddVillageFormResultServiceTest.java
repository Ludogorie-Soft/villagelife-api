package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.*;
import com.example.ludogoriesoft.village.enums.Consents;
import com.example.ludogoriesoft.village.enums.Distance;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class AddVillageFormResultServiceTest {
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
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

    @Test
    public void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSIsEmpty() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setVillageLivingConditionDTOS(new ArrayList<>());

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult);

        verifyNoInteractions(villageLivingConditionService);
    }

    @Test
    public void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSHasNullConsents() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillageLivingConditionDTO> dtos = new ArrayList<>();
        dtos.add(new VillageLivingConditionDTO(null, villageId, 1L, null));
        addVillageFormResult.setVillageLivingConditionDTOS(dtos);

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult);

        verifyNoInteractions(villageLivingConditionService);
    }


    @Test
    public void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSHasNonNullConsents() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillageLivingConditionDTO> dtos = new ArrayList<>();
        dtos.add(new VillageLivingConditionDTO());
        dtos.add(new VillageLivingConditionDTO(null, villageId, 1L, Consents.COMPLETELY_AGREED));
        addVillageFormResult.setVillageLivingConditionDTOS(dtos);

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(villageLivingConditionService, times(1)).createVillageLivingCondition(any(VillageLivingConditionDTO.class));
    }
    @Test
    public void testCreateVillageLivingConditionFromAddVillageFormResultWhenVillageLivingConditionDTOSHasMixedConsents() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillageLivingConditionDTO> dtos = new ArrayList<>();

        VillageLivingConditionDTO dto1 = new VillageLivingConditionDTO(null, villageId, 1L, Consents.COMPLETELY_AGREED);
        VillageLivingConditionDTO dto2 = new VillageLivingConditionDTO(null, villageId, 2L, Consents.DISAGREE);
        VillageLivingConditionDTO dto3 = new VillageLivingConditionDTO(null, villageId, 3L, Consents.CANT_DECIDE);

        dtos.add(new VillageLivingConditionDTO());
        dtos.add(dto1);
        dtos.add(dto2);
        dtos.add(dto3);

        addVillageFormResult.setVillageLivingConditionDTOS(dtos);

        addVillageFormResultService.createVillageLivingConditionFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(villageLivingConditionService, times(3)).createVillageLivingCondition(any(VillageLivingConditionDTO.class));
        verify(villageLivingConditionService).createVillageLivingCondition(dto1);
        verify(villageLivingConditionService).createVillageLivingCondition(dto2);
        verify(villageLivingConditionService).createVillageLivingCondition(dto3);
    }


    @Test
    public void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSIsEmpty() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setVillagePopulationAssertionDTOS(new ArrayList<>());

        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult);

        verifyNoInteractions(villagePopulationAssertionService);
    }
    @Test
    public void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSHasNullAnswers() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillagePopulationAssertionDTO> dtos = new ArrayList<>();
        dtos.add(new VillagePopulationAssertionDTO(null, villageId, 1L, null));
        addVillageFormResult.setVillagePopulationAssertionDTOS(dtos);

        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult);

        verifyNoInteractions(villagePopulationAssertionService);
    }
    @Test
    public void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSHasNonNullAnswers() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillagePopulationAssertionDTO> dtos = new ArrayList<>();

        VillagePopulationAssertionDTO dto1 = new VillagePopulationAssertionDTO(null, villageId, 1L, Consents.DISAGREE);
        VillagePopulationAssertionDTO dto2 = new VillagePopulationAssertionDTO(null, villageId, 2L, Consents.RATHER_AGREE);

        dtos.add(new VillagePopulationAssertionDTO());
        dtos.add(dto1);
        dtos.add(dto2);

        addVillageFormResult.setVillagePopulationAssertionDTOS(dtos);

        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(villagePopulationAssertionService, times(2)).createVillagePopulationAssertionDTO(any(VillagePopulationAssertionDTO.class));
        verify(villagePopulationAssertionService).createVillagePopulationAssertionDTO(dto1);
        verify(villagePopulationAssertionService).createVillagePopulationAssertionDTO(dto2);
    }

    @Test
    public void testCreateVillagePopulationAssertionsFromAddVillageFormResultWhenVillagePopulationAssertionDTOSHasMixedAnswers() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<VillagePopulationAssertionDTO> dtos = new ArrayList<>();

        VillagePopulationAssertionDTO dto1 = new VillagePopulationAssertionDTO(null, villageId, 1L, Consents.CANT_DECIDE);
        VillagePopulationAssertionDTO dto2 = new VillagePopulationAssertionDTO(null, villageId, 2L, null);
        VillagePopulationAssertionDTO dto3 = new VillagePopulationAssertionDTO(null, villageId, 3L, Consents.COMPLETELY_AGREED);

        dtos.add(new VillagePopulationAssertionDTO());
        dtos.add(dto1);
        dtos.add(dto2);
        dtos.add(dto3);

        addVillageFormResult.setVillagePopulationAssertionDTOS(dtos);

        addVillageFormResultService.createVillagePopulationAssertionsFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(villagePopulationAssertionService, times(2)).createVillagePopulationAssertionDTO(any(VillagePopulationAssertionDTO.class));
        verify(villagePopulationAssertionService).createVillagePopulationAssertionDTO(dto1);
        verify(villagePopulationAssertionService).createVillagePopulationAssertionDTO(dto3);
        verifyNoMoreInteractions(villagePopulationAssertionService);
    }


    @Test
    public void testCreateObjectVillagesFromAddVillageFormResult() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();

        ObjectVillageDTO dto1 = new ObjectVillageDTO(null, villageId, 1L, Distance.IN_THE_VILLAGE);
        ObjectVillageDTO dto2 = new ObjectVillageDTO(null, villageId, 2L, Distance.ON_10_KM);
        ObjectVillageDTO dto3 = new ObjectVillageDTO(null, villageId, 3L, null);

        objectVillageDTOS.add(new ObjectVillageDTO());
        objectVillageDTOS.add(dto1);
        objectVillageDTOS.add(dto2);
        objectVillageDTOS.add(dto3);

        addVillageFormResult.setObjectVillageDTOS(objectVillageDTOS);

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(objectVillageService, times(2)).createObjectVillage(any(ObjectVillageDTO.class));
        verify(objectVillageService).createObjectVillage(dto1);
        verify(objectVillageService).createObjectVillage(dto2);
        verify(objectVillageService, never()).createObjectVillage(dto3);
    }

    @Test
    public void testCreateObjectVillagesFromAddVillageFormResultNoObjectVillageDTOS() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setObjectVillageDTOS(new ArrayList<>()); // Set an empty list instead of null

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(objectVillageService, never()).createObjectVillage(any(ObjectVillageDTO.class));
    }


    @Test
    public void testCreateObjectVillagesFromAddVillageFormResultEmptyObjectVillageDTOS() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();

        addVillageFormResult.setObjectVillageDTOS(objectVillageDTOS);

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(objectVillageService, never()).createObjectVillage(any(ObjectVillageDTO.class));
    }

    @Test
    public void testCreateObjectVillagesFromAddVillageFormResultNullDistance() {
        Long villageId = 12345L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();

        ObjectVillageDTO dto1 = new ObjectVillageDTO(null, villageId, 1L, Distance.ON_31_TO_50_KM);
        ObjectVillageDTO dto2 = new ObjectVillageDTO(null, villageId, 2L, null);

        objectVillageDTOS.add(new ObjectVillageDTO());
        objectVillageDTOS.add(dto1);
        objectVillageDTOS.add(dto2);

        addVillageFormResult.setObjectVillageDTOS(objectVillageDTOS);

        addVillageFormResultService.createObjectVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        verify(objectVillageService, times(1)).createObjectVillage(dto1);
        verify(objectVillageService, never()).createObjectVillage(dto2);
    }
    @Test
    public void createVillageAnswerQuestionsFromAddVillageFormResultValidDataCreatesVillageAnswerQuestions() {
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
    public void createVillageAnswerQuestionsFromAddVillageFormResultEmptyQuestionResponsesNoVillageAnswerQuestionsCreated() {
        Long villageId = 1L;
        List<String> questionResponses = Collections.emptyList();
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setQuestionResponses(questionResponses);

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult);

        Mockito.verify(villageAnswerQuestionService, Mockito.never()).createVillageAnswerQuestion(Mockito.any(VillageAnswerQuestionDTO.class));
    }

    //@Test
    //public void createVillageAnswerQuestionsFromAddVillageFormResultNullAddVillageFormResultNoVillageAnswerQuestionsCreated() {
    //    Long villageId = 1L;
    //    AddVillageFormResult addVillageFormResult = null;
//
    //    addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult);
//
    //    Mockito.verify(villageAnswerQuestionService, Mockito.never()).createVillageAnswerQuestion(Mockito.any(VillageAnswerQuestionDTO.class));
    //}


    @Test
    public void createVillageAnswerQuestionsFromAddVillageFormResultNullQuestionResponsesNoVillageAnswerQuestionsCreated() {
        Long villageId = 1L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setQuestionResponses(new ArrayList<>());

        addVillageFormResultService.createVillageAnswerQuestionsFromAddVillageFormResult(villageId, addVillageFormResult);

        Mockito.verify(villageAnswerQuestionService, Mockito.never()).createVillageAnswerQuestion(Mockito.any(VillageAnswerQuestionDTO.class));
    }

    @Test
    public void createEthnicityVillagesFromAddVillageFormResultValidDataEthnicityVillagesCreated() {
        Long villageId = 1L;
        List<Long> ethnicityDTOIds = Arrays.asList(1L, 2L, 3L);
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setEthnicityDTOIds(ethnicityDTOIds);

        addVillageFormResultService.createEthnicityVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        for (Long id : ethnicityDTOIds) {
            EthnicityVillageDTO expectedDTO = new EthnicityVillageDTO(null, villageId, id);
            Mockito.verify(ethnicityVillageService).createEthnicityVillage(expectedDTO);
        }
    }

    @Test
    public void createEthnicityVillagesFromAddVillageFormResultEmptyEthnicityDTOIdsNoEthnicityVillagesCreated() {
        Long villageId = 1L;
        List<Long> ethnicityDTOIds = Collections.emptyList();
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setEthnicityDTOIds(ethnicityDTOIds);

        addVillageFormResultService.createEthnicityVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        Mockito.verify(ethnicityVillageService, Mockito.never()).createEthnicityVillage(Mockito.any(EthnicityVillageDTO.class));
    }

    @Test
    public void createEthnicityVillagesFromAddVillageFormResultLargeNumberOfEthnicityDTOIdsEthnicityVillagesCreated() {
        Long villageId = 1L;
        List<Long> ethnicityDTOIds = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            ethnicityDTOIds.add(i);
        }
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setEthnicityDTOIds(ethnicityDTOIds);

        addVillageFormResultService.createEthnicityVillagesFromAddVillageFormResult(villageId, addVillageFormResult);

        for (Long id : ethnicityDTOIds) {
            EthnicityVillageDTO expectedDTO = new EthnicityVillageDTO(null, villageId, id);
            Mockito.verify(ethnicityVillageService).createEthnicityVillage(expectedDTO);
        }
    }
    @Test
    public void createVillageGroundCategoryFromAddVillageFormResultTest() {
        Long villageId = 1L;
        AddVillageFormResult addVillageFormResult = new AddVillageFormResult();
        addVillageFormResult.setGroundCategoryName("Ground Category Name");

        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(2L);
        Mockito.when(groundCategoryService.getByGroundCategoryName(addVillageFormResult.getGroundCategoryName())).thenReturn(groundCategoryDTO);

        addVillageFormResultService.createVillageGroundCategoryFromAddVillageFormResult(villageId, addVillageFormResult);

        ArgumentCaptor<VillageGroundCategoryDTO> villageGroundCategoryDTOCaptor = ArgumentCaptor.forClass(VillageGroundCategoryDTO.class);
        Mockito.verify(villageGroundCategoryService).createVillageGroundCategoryDTO(villageGroundCategoryDTOCaptor.capture());

        VillageGroundCategoryDTO capturedDTO = villageGroundCategoryDTOCaptor.getValue();
        assertEquals(villageId, capturedDTO.getVillageId());
        assertEquals(groundCategoryDTO.getId(), capturedDTO.getGroundCategoryId());
    }
}
