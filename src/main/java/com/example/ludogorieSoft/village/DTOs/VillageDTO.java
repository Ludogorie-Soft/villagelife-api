package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Model.Population;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToOne;
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
