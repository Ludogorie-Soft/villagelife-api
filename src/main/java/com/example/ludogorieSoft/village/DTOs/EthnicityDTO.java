package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Model.Ethnicity;
import com.example.ludogorieSoft.village.Model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EthnicityDTO {

    private Long id;
    private Village village;
    private Ethnicity ethnicity;
}
