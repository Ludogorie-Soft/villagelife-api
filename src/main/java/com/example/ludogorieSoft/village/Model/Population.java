package com.example.ludogorieSoft.village.Model;


import com.example.ludogorieSoft.village.Enums.Children;
import com.example.ludogorieSoft.village.Enums.Foreigners;
import com.example.ludogorieSoft.village.Enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.Enums.Residents;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "populations")
public class Population {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NumberOfPopulation numberOfPopulation;
    @Enumerated(EnumType.STRING)
    private Residents residents;
    @Enumerated(EnumType.STRING)
    private Children children;
    @Enumerated(EnumType.STRING)
    private Foreigners foreigners;
}
