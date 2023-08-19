package com.example.ludogorieSoft.village.services;

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
