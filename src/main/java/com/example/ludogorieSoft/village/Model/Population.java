package com.example.ludogorieSoft.village.Model;


import com.example.ludogorieSoft.village.Enums.ChildrenUpTo14Years;
import com.example.ludogorieSoft.village.Enums.Foreigners;
import com.example.ludogorieSoft.village.Enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.Enums.ResidentsUpTo50Years;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "population")
public class Population {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NumberOfPopulation numberOfPopulation;
    @Enumerated(EnumType.STRING)
    private ResidentsUpTo50Years residentsUpTo50Years;
    @Enumerated(EnumType.STRING)
    private ChildrenUpTo14Years childrenUpTo14Years;
    @Enumerated(EnumType.STRING)
    private Foreigners foreigners;
}
