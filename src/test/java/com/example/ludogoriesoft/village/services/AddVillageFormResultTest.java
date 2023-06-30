package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AddVillageFormResultTest {

    @Test
    void testAddVillageFormResult() {
        // Create sample data
        VillageDTO villageDTO = new VillageDTO();
        PopulationDTO populationDTO = new PopulationDTO();
        String groundCategoryName = "Ground Category";
        List<Long> ethnicityDTOIds = new ArrayList<>();
        List<String> questionResponses = new ArrayList<>();
        List<VillageLivingConditionDTO> villageLivingConditionDTOS = new ArrayList<>();
        List<ObjectVillageDTO> objectVillageDTOS = new ArrayList<>();
        List<VillagePopulationAssertionDTO> villagePopulationAssertionDTOS = new ArrayList<>();
        List<byte[]> imageBytes = new ArrayList<>();

        // Create an instance of AddVillageFormResult
        AddVillageFormResult formResult = new AddVillageFormResult(
                villageDTO,
                populationDTO,
                groundCategoryName,
                ethnicityDTOIds,
                questionResponses,
                villageLivingConditionDTOS,
                objectVillageDTOS,
                villagePopulationAssertionDTOS,
                imageBytes
        );

        Assertions.assertEquals(villageDTO, formResult.getVillageDTO());
        Assertions.assertEquals(populationDTO, formResult.getPopulationDTO());
        Assertions.assertEquals(groundCategoryName, formResult.getGroundCategoryName());
        Assertions.assertEquals(ethnicityDTOIds, formResult.getEthnicityDTOIds());
        Assertions.assertEquals(questionResponses, formResult.getQuestionResponses());
        Assertions.assertEquals(villageLivingConditionDTOS, formResult.getVillageLivingConditionDTOS());
        Assertions.assertEquals(objectVillageDTOS, formResult.getObjectVillageDTOS());
        Assertions.assertEquals(villagePopulationAssertionDTOS, formResult.getVillagePopulationAssertionDTOS());
        Assertions.assertEquals(imageBytes, formResult.getImageBytes());
    }
}
