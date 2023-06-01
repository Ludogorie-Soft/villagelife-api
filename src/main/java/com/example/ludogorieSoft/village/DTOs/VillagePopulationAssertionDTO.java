package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Model.PopulatedAssertion;
import com.example.ludogorieSoft.village.Model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillagePopulationAssertionDTO {

        private Long id;
        private Village villageID;
        private PopulatedAssertion populatedAssertionID;
        private String answer;


}
