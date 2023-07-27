package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageDTO {

    private Long id;
    private String name;
    private String region;
    private int populationCount;
    private PopulationDTO populationDTO;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateUpload;
    private boolean status;

    private List<LivingConditionDTO> livingConditions;
    private List<ObjectAroundVillageDTO> object;
    private List<String> images;

//    private List<ObjectVillageDTO> objectVillages;
//    private List<VillageLivingConditionDTO> villageLivingConditions;
//    private List<VillageAnswerQuestionDTO> villageAnswerQuestions;
//    private List<EthnicityVillageDTO> ethnicityVillages;
//    private List<VillageGroundCategoryDTO> villageGroundCategories;
//    private List<VillageImageDTO> villageImages;
//    private List<VillageLandscapeDTO> villageLandscapes;
//    private List<VillagePopulationAssertionDTO> villagePopulationAssertions;

}
