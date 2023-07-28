package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageAnswerQuestionDTO {

    private Long id;
    private Long villageId;
    private Long questionId;
    private String answer;
    private Boolean status;
    private LocalDateTime dateUpload;
}
