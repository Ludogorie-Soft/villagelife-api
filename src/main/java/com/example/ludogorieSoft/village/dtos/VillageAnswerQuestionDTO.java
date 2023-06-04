package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageAnswerQuestionDTO {

    private Long id;
    private Long villageId;
    private Long questionId;
    private String answer;
}
