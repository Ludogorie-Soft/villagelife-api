package com.example.ludogorieSoft.village.dtos.response;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VillageInfo {
    private VillageDTO villageDTO;
    private PopulationDTO populationDTO;
    private List<String> ethnicities;
    private String groundCategories;
    private List<PopulationAssertionResponse> populationAssertionResponses;
    private List<LivingConditionResponse> livingConditionResponses;
    private List<ObjectVillageResponse> objectVillageResponses;
    private List<AnswersQuestionResponse> answersQuestionResponses;
}
