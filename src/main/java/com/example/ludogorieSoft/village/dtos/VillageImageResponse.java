package com.example.ludogorieSoft.village.dtos;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VillageImageResponse {
    private VillageDTO villageDTO;
    private List<String> images;
}
