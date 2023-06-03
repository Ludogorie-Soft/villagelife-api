package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.model.Landscape;
import com.example.ludogorieSoft.village.model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageLandscapeDTO {

    private Long id;
    private Long villageId;
    private Long landscapeId;

}
