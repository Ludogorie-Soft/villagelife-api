package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.enums.Distance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectVillageDTO {

    private Long id;
    private Long villageId;
    private Long objectAroundVillageId;
    private Distance distance;
    private Boolean status;
    private LocalDateTime dateUpload;

}
