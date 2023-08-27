package com.example.ludogorieSoft.village.services;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.verify;

class AdminVillageServiceTest {

    @InjectMocks
    private AdminVillageService adminVillageService;

    @Mock
    private VillagePopulationAssertionService villagePopulationAssertionService;

    @Mock
    private VillageLivingConditionService villageLivingConditionService;

    @Mock
    private VillageImageService villageImageService;

    @Mock
    private VillageAnswerQuestionService villageAnswerQuestionService;

    @Mock
    private ObjectVillageService objectVillageService;

    @Mock
    private EthnicityVillageService ethnicityVillageService;

    @Mock
    private VillageGroundCategoryService villageGroundCategoryService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private VillageService villageService;
    @Mock
    private PopulationService populationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUpdateVillageStatusAndVillageResponsesStatus() {
        Long villageId = 1L;
        String answerDate = "2023-08-10";

        boolean status = false;

        doNothing().when(villageService).increaseApprovedResponsesCount(villageId);
        adminVillageService.updateVillageStatusAndVillageResponsesStatus(villageId, answerDate);

        verify(villagePopulationAssertionService).updateVillagePopulationAssertionStatus(villageId, status, answerDate);
        verify(villageLivingConditionService).updateVillageLivingConditionStatus(villageId, status, answerDate);
        verify(villageImageService).updateVillageImagesStatus(villageId, status, answerDate);
        verify(villageAnswerQuestionService).updateVillageAnswerQuestionStatus(villageId, status, answerDate);
        verify(objectVillageService).updateObjectVillageStatus(villageId, status, answerDate);
        verify(ethnicityVillageService).updateEthnicityVillageStatus(villageId, status, answerDate);
        verify(villageGroundCategoryService).updateVillageGroundCategoryStatus(villageId, status, answerDate);
    }

    @Test
    void testGetUnapprovedVillageResponsesWithSortedAnswers() {
        Village village1 = new Village();
        village1.setId(1L);
        Village village2 = new Village();
        village2.setId(2L);
        List<Village> villages = new ArrayList<>();
        villages.add(village1);
        villages.add(village2);
        when(villageRepository.findAll()).thenReturn(villages);

        List<VillageResponse> responses = adminVillageService.getUnapprovedVillageResponsesWithSortedAnswers(true);

        Assertions.assertEquals(2, responses.size());
    }

    @Test
    void testGetRejectedVillageResponsesWithSortedAnswers() {
        Village village1 = new Village();
        village1.setId(1L);

        Village village2 = new Village();
        village2.setId(2L);

        List<Village> villages = new ArrayList<>();
        villages.add(village1);
        villages.add(village2);

        when(villageRepository.findAllVillagesWithRejectedResponses()).thenReturn(villages);

        Population population1 = new Population();
        population1.setVillage(village1);
        population1.setDateUpload(now());

        Population population2 = new Population();
        population2.setVillage(village2);
        population2.setDateUpload(now());

        List<Population> mockedPopulationData = new ArrayList<>();
        mockedPopulationData.add(population1);
        mockedPopulationData.add(population2);
        when(populationService.findByVillageIdAndVillageStatusDateDeleteNotNull(anyLong(), anyBoolean())).thenReturn(mockedPopulationData);

        List<VillageResponse> responses = adminVillageService.getRejectedVillageResponsesWithSortedAnswers(true);

        Assertions.assertEquals(mockedPopulationData.size(), responses.size());
    }

    @Test
    void testCreateVillageResponseRejected() {
        Village village = new Village();
        boolean status = false;
        String keyWord = "rejected";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Population> mockedPopulationData = new ArrayList<>();
        Population population = new Population();
        population.setDateUpload(LocalDateTime.parse("2023-08-15T10:00:00"));
        mockedPopulationData.add(population);
        when(populationService.findByVillageIdAndVillageStatusDateDeleteNotNull(anyLong(), anyBoolean())).thenReturn(mockedPopulationData);

        VillageResponse response = adminVillageService.createVillageResponse(village, formatter, status, keyWord);

        assertEquals(village.getId(), response.getId());
        assertEquals(village.getName(), response.getName());
        assertEquals(1, response.getGroupedAnswers().size());
    }

    @Test
    void testGetFormattedDatesFromAnswers() {
        List<Population> mockedPopulationData = new ArrayList<>();
        Population answer1 = new Population();
        answer1.setDateUpload(LocalDateTime.parse("2023-08-15T10:00:00"));
        mockedPopulationData.add(answer1);
        Population answer2 = new Population();
        answer2.setDateUpload(LocalDateTime.parse("2023-08-15T11:00:00"));
        mockedPopulationData.add(answer2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<String> formattedDates = adminVillageService.getFormattedDatesFromAnswers(mockedPopulationData, formatter);

        assertEquals(2, formattedDates.size());
        assertEquals("2023-08-15 10:00:00", formattedDates.get(0));
        assertEquals("2023-08-15 11:00:00", formattedDates.get(1));
    }
    @Test
     void testGetRejectedAnswersForVillage() {
        Long villageId = 1L;
        boolean status = false;

        Population population1 = new Population();
        population1.setId(1L);

        Population population2 = new Population();
        population2.setId(2L);

        List<Population> mockedPopulationData = new ArrayList<>();
        mockedPopulationData.add(population1);
        mockedPopulationData.add(population2);

        when(populationService.findByVillageIdAndVillageStatusDateDeleteNotNull(anyLong(), anyBoolean())).thenReturn(mockedPopulationData);

        List<Population> result = adminVillageService.getRejectedAnswersForVillage(villageId, status);

        assertEquals(mockedPopulationData, result);
    }

    @Test
     void testGetAnswersToApprove() {
        Long villageId = 1L;
        boolean status = true;

        Population population1 = new Population();
        population1.setId(1L);

        Population population2 = new Population();
        population2.setId(2L);

        List<Population> mockedPopulationData = new ArrayList<>();
        mockedPopulationData.add(population1);
        mockedPopulationData.add(population2);
        when(populationService.findByVillageIdAndVillageStatus(anyLong(), anyBoolean())).thenReturn(mockedPopulationData);

        List<Population> result = adminVillageService.getAnswersToApprove(villageId, status);

        assertEquals(mockedPopulationData, result);
    }

    @Test
    void testRejectVillageResponses() {
        Long villageId = 1L;
        String answerDate = "2023-08-27 10:00:00";
        LocalDateTime timestamp = LocalDateTime.of(2023, 8, 27, 10, 0, 0);
        boolean status = false;

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(villageId);
        villageDTO.setStatus(false);

        Village village = new Village();
        village.setId(villageId);
        village.setStatus(false);

        when(villageService.getVillageById(villageId)).thenReturn(villageDTO);
        when(villageRepository.findById(villageId)).thenReturn(Optional.of(village));
        when(villageService.updateVillageStatus(eq(villageId), any(VillageDTO.class))).thenReturn(villageDTO);

        doNothing().when(populationService).rejectPopulationResponse(villageId, status, answerDate, timestamp);
        doNothing().when(villagePopulationAssertionService).rejectVillagePopulationAssertionStatus(villageId, status, answerDate, timestamp);
        doNothing().when(villageLivingConditionService).rejectVillageLivingConditionResponse(villageId, status, answerDate, timestamp);
        doNothing().when(villageImageService).rejectVillageImages(villageId, status, answerDate, timestamp);
        doNothing().when(villageAnswerQuestionService).rejectVillageAnswerQuestionResponse(villageId, status, answerDate, timestamp);
        doNothing().when(objectVillageService).rejectObjectVillageResponse(villageId, status, answerDate, timestamp);
        doNothing().when(ethnicityVillageService).rejectEthnicityVillageResponse(villageId, status, answerDate, timestamp);
        doNothing().when(villageGroundCategoryService).rejectVillageGroundCategoryResponse(villageId, status, answerDate, timestamp);

        adminVillageService.rejectVillageResponses(villageId, answerDate);

        verify(villageService).getVillageById(villageId);
        verify(populationService).rejectPopulationResponse(eq(villageId), eq(status), eq(answerDate), argThat(Objects::nonNull));
        verify(villagePopulationAssertionService).rejectVillagePopulationAssertionStatus(eq(villageId), eq(status), eq(answerDate), argThat(Objects::nonNull));
        verify(villageLivingConditionService).rejectVillageLivingConditionResponse(eq(villageId), eq(status), eq(answerDate), argThat(Objects::nonNull));
        verify(villageImageService).rejectVillageImages(eq(villageId), eq(status), eq(answerDate), argThat(Objects::nonNull));
        verify(villageAnswerQuestionService).rejectVillageAnswerQuestionResponse(eq(villageId), eq(status), eq(answerDate), argThat(Objects::nonNull));
        verify(objectVillageService).rejectObjectVillageResponse(eq(villageId), eq(status), eq(answerDate), argThat(Objects::nonNull));
        verify(ethnicityVillageService).rejectEthnicityVillageResponse(eq(villageId), eq(status), eq(answerDate), argThat(Objects::nonNull));
        verify(villageGroundCategoryService).rejectVillageGroundCategoryResponse(eq(villageId), eq(status), eq(answerDate), argThat(Objects::nonNull));
    }


}
