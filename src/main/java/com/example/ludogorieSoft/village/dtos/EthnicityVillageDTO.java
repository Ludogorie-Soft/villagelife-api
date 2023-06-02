package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EthnicityVillageDTO {

    private Long id;
    //private Village village;
    //private Ethnicity ethnicity;
    private Long villageId;
    private Long ethnicityId;
}
