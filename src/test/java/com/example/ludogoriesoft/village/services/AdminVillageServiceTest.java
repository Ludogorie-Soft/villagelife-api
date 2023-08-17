package com.example.ludogorieSoft.village.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.*;
import static java.time.LocalDateTime.*;


class AdminVillageServiceTest {

    @InjectMocks
    private AdminVillageService adminVillageService;

    @Mock
    private VillageRepository villageRepository;

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
}
