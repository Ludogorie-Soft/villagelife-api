package com.example.ludogorieSoft.village.dtos.response;

import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.model.Population;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VillageResponse {

    private Long id;

    private String name;

    private Population population;

    private LocalDateTime dateUpload;

    private boolean status;

    private Administrator admin;

    private LocalDateTime dateApproved;

}
