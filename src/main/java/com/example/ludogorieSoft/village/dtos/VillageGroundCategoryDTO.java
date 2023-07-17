package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageGroundCategoryDTO {

    private Long id;
    private Long villageId;
    private Long groundCategoryId;
}
