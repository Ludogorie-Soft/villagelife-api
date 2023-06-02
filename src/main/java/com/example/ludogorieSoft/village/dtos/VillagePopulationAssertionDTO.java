package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import com.example.ludogorieSoft.village.model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillagePopulationAssertionDTO {

        private Long id;
        private Village villageID;
        private PopulatedAssertion populatedAssertionID;
        private String answer;

        //  private Consents consents;


}
