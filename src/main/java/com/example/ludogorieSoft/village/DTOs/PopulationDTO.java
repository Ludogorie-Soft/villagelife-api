package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Enums.ChildrenUpTo14Years;
import com.example.ludogorieSoft.village.Enums.Foreigners;
import com.example.ludogorieSoft.village.Enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.Enums.ResidentsUpTo50Years;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopulationDTO {

    private Long id;
    private NumberOfPopulation numberOfPopulation;
    private ResidentsUpTo50Years residentsUpTo50Years;
    private ChildrenUpTo14Years childrenUpTo14Years;
    private Foreigners foreigners;
}
