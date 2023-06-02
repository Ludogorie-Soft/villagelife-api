package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Model.Landscape;
import com.example.ludogorieSoft.village.Model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageLandscapeDTO {

    private Long id;
    private Village village;
    private Landscape landscape;

}
