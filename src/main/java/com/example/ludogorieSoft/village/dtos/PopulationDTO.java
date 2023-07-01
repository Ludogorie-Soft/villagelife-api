package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.enums.Residents;
import com.example.ludogorieSoft.village.enums.Children;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopulationDTO {

    private Long id;
    private NumberOfPopulation numberOfPopulation;
    private Residents residents;
    private Children children;
    private Foreigners foreigners;
}
