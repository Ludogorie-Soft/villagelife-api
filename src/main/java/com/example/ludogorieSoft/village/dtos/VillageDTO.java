package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageDTO {

    private Long id;
    private String name;
    //private Population population;
//    private PopulationDTO populationDTO;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateUpload;
    private boolean status;

}
