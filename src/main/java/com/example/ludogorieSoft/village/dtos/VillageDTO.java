package com.example.ludogorieSoft.village.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VillageDTO {

    private Long id;
    private String name;
    private String region;
    private int populationCount;
    private PopulationDTO populationDTO;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateUpload;
    private Boolean status;

    private List<LivingConditionDTO> livingConditions;
    private List<ObjectAroundVillageDTO> object;
    private List<String> images;

    private List<ObjectVillageDTO> objectVillages;
    private List<VillageLivingConditionDTO> villageLivingConditions;
    private List<VillageAnswerQuestionDTO> villageAnswerQuestions;
    private List<EthnicityVillageDTO> ethnicityVillages;
    private List<VillageGroundCategoryDTO> villageGroundCategories;
    private List<VillageLandscapeDTO> villageLandscapes;
    private List<VillagePopulationAssertionDTO> villagePopulationAssertions;

}
