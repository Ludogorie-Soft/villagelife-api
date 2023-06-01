package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Model.Questions;
import com.example.ludogorieSoft.village.Model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageAnswerQuestionDTO {

    private Long id;
    private Village villageID;
    private Questions questionsID;
    private String answer;
}
