package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.model.Population;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageDTO {

    private Long id;
    private String name;
    private Population population;
    private Date dateUpload;
    private boolean status;

}
