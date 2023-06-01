package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Model.GroundCategory;
import com.example.ludogorieSoft.village.Model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageGroundCategoryDTO {

    private Long id;
    private Village villageID;
    private GroundCategory groundCategory;
}
