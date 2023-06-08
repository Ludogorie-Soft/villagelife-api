package com.example.ludogoriesoft.village.dtos;

import com.example.ludogoriesoft.village.enums.Foreigners;
import com.example.ludogoriesoft.village.enums.NumberOfPopulation;
import com.example.ludogoriesoft.village.enums.Residents;
import com.example.ludogoriesoft.village.enums.Children;
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
