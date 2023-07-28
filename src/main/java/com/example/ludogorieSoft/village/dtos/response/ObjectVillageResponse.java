package com.example.ludogorieSoft.village.dtos.response;

import com.example.ludogorieSoft.village.enums.Distance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectVillageResponse {
    private Distance distance;
    private String objects;
}
