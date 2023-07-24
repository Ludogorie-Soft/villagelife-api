package com.example.ludogorieSoft.village.dtos.response;

import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VillageInfo {
    private VillageDTO villageDTO;
    private String ethnicities;
    private List<PopulationAssertionResponse> populationAssertionResponses;
    private List<LivingConditionResponse> livingConditionResponses;
    private List<ObjectVillageDTO> objectVillages;
    private List<VillageAnswerQuestionDTO> villageAnswerQuestionDTOs;
}
