package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageAnswerQuestionDTO {

    private Long id;
    private Village village;
    private Question question;
    private String answer;
}
