package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageImageDTO {
    private Long id;
    private Long villageId;
    private String imageName;
    private Boolean status;
    private LocalDateTime dateUpload;
}
