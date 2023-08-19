package com.example.ludogorieSoft.village.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.model.EthnicityVillage;
import com.example.ludogorieSoft.village.model.Region;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private VillageService villageService;
    @Mock
    private TimestampUtils timestampUtils;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUpdateVillageStatusAndVillageResponsesStatus() {
        Long villageId = 1L;
        String answerDate = "2023-08-10";

        boolean status = false;

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
        EthnicityVillage ethnicityVillage1 = new EthnicityVillage();
        ethnicityVillage1.setVillage(village1);
        ethnicityVillage1.setDateUpload(now());
        EthnicityVillage ethnicityVillage2 = new EthnicityVillage();
        ethnicityVillage2.setVillage(village2);
        ethnicityVillage2.setDateUpload(now());
        List<EthnicityVillage> mockedEthnicityVillageData = new ArrayList<>();
        mockedEthnicityVillageData.add(ethnicityVillage1);
        mockedEthnicityVillageData.add(ethnicityVillage2);
        when(ethnicityVillageService.findByVillageIdAndVillageStatusDateDeleteNotNull(anyLong(), anyBoolean())).thenReturn(mockedEthnicityVillageData);

        List<VillageResponse> responses = adminVillageService.getRejectedVillageResponsesWithSortedAnswers(true);

        Assertions.assertEquals(mockedEthnicityVillageData.size(), responses.size());
    }

    @Test
    void testCreateVillageResponseRejected() {
        Village village = new Village();
        boolean status = false;
        String keyWord = "rejected";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<EthnicityVillage> mockedEthnicityVillageData = new ArrayList<>();
        EthnicityVillage ethnicityVillage = new EthnicityVillage();
        ethnicityVillage.setDateUpload(LocalDateTime.parse("2023-08-15T10:00:00"));
        mockedEthnicityVillageData.add(ethnicityVillage);
        when(ethnicityVillageService.findByVillageIdAndVillageStatusDateDeleteNotNull(anyLong(), anyBoolean())).thenReturn(mockedEthnicityVillageData);

        VillageResponse response = adminVillageService.createVillageResponse(village, formatter, status, keyWord);

        assertEquals(village.getId(), response.getId());
        assertEquals(village.getName(), response.getName());
        assertEquals(1, response.getGroupedAnswers().size());
    }

    @Test
    void testGetFormattedDatesFromAnswers() {
        List<EthnicityVillage> mockedEthnicityVillageData = new ArrayList<>();
        EthnicityVillage answer1 = new EthnicityVillage();
        answer1.setDateUpload(LocalDateTime.parse("2023-08-15T10:00:00"));
        mockedEthnicityVillageData.add(answer1);
        EthnicityVillage answer2 = new EthnicityVillage();
        answer2.setDateUpload(LocalDateTime.parse("2023-08-15T11:00:00"));
        mockedEthnicityVillageData.add(answer2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<String> formattedDates = adminVillageService.getFormattedDatesFromAnswers(mockedEthnicityVillageData, formatter);

        assertEquals(2, formattedDates.size());
        assertEquals("2023-08-15 10:00:00", formattedDates.get(0));
        assertEquals("2023-08-15 11:00:00", formattedDates.get(1));
    }
    @Test
     void testGetRejectedAnswersForVillage() {
        Long villageId = 1L;
        boolean status = false;
        EthnicityVillage ethnicityVillage1 = new EthnicityVillage();
        ethnicityVillage1.setId(1L);
        EthnicityVillage ethnicityVillage2 = new EthnicityVillage();
        ethnicityVillage2.setId(2L);
        List<EthnicityVillage> mockedEthnicityVillageData = new ArrayList<>();
        mockedEthnicityVillageData.add(ethnicityVillage1);
        mockedEthnicityVillageData.add(ethnicityVillage2);
        when(ethnicityVillageService.findByVillageIdAndVillageStatusDateDeleteNotNull(anyLong(), anyBoolean())).thenReturn(mockedEthnicityVillageData);

        List<EthnicityVillage> result = adminVillageService.getRejectedAnswersForVillage(villageId, status);

        assertEquals(mockedEthnicityVillageData, result);
    }

    @Test
     void testGetAnswersToApprove() {
        Long villageId = 1L;
        boolean status = true;
        EthnicityVillage ethnicityVillage1 = new EthnicityVillage();
        ethnicityVillage1.setId(1L);
        EthnicityVillage ethnicityVillage2 = new EthnicityVillage();
        ethnicityVillage2.setId(2L);
        List<EthnicityVillage> mockedEthnicityVillageData = new ArrayList<>();
        mockedEthnicityVillageData.add(ethnicityVillage1);
        mockedEthnicityVillageData.add(ethnicityVillage2);
        when(ethnicityVillageService.findByVillageIdAndVillageStatus(anyLong(), anyBoolean())).thenReturn(mockedEthnicityVillageData);

        List<EthnicityVillage> result = adminVillageService.getAnswersToApprove(villageId, status);

        assertEquals(mockedEthnicityVillageData, result);
    }
//    @Test
//    public void testRejectVillageResponses() {
//
//        Long villageId = 1L;
//        String answerDate = "2023-08-15 18:12:52";
//        LocalDateTime timestamp = LocalDateTime.of(2023, 8, 15, 12, 12,52); // Mock timestamp
//        VillageDTO villageDTO = new VillageDTO();
//        villageDTO.setStatus(true);
//        when(villageService.getVillageById(villageId)).thenReturn(villageDTO);
//        when(timestampUtils.getCurrentTimestamp()).thenReturn(timestamp);
//
//        adminVillageService.rejectVillageResponses(villageId, answerDate);
//
//        verify(villageService).updateVillageStatus(eq(villageId), eq(villageDTO));
//        verify(villagePopulationAssertionService).rejectVillagePopulationAssertionStatus(eq(villageId), eq(false), eq(answerDate), eq(timestamp));
//        verify(villageLivingConditionService).rejectVillageLivingConditionResponse(eq(villageId), eq(false), eq(answerDate), eq(timestamp));
//        verify(villageImageService).rejectVillageImageResponse(eq(villageId), eq(false), eq(answerDate), eq(timestamp));
//        verify(villageAnswerQuestionService).rejectVillageAnswerQuestionResponse(eq(villageId), eq(false), eq(answerDate), eq(timestamp));
//        verify(objectVillageService).rejectObjectVillageResponse(eq(villageId), eq(false), eq(answerDate), eq(timestamp));
//        verify(ethnicityVillageService).rejectEthnicityVillageResponse(eq(villageId), eq(false), eq(answerDate), eq(timestamp));
//        verify(villageGroundCategoryService).rejectVillageGroundCategoryResponse(eq(villageId), eq(false), eq(answerDate), eq(timestamp));
//    }
}
