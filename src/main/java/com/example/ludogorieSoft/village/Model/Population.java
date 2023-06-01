package com.example.ludogorieSoft.village.Model;


import com.example.ludogorieSoft.village.Enums.ChildrenUpTo14Years;
import com.example.ludogorieSoft.village.Enums.Foreigners;
import com.example.ludogorieSoft.village.Enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.Enums.ResidentsUpTo50Years;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "populations")
public class Population {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private NumberOfPopulation numberOfPopulation;
    private ResidentsUpTo50Years residentsUpTo50Years;
    private ChildrenUpTo14Years childrenUpTo14Years;
    private Foreigners foreigners;
}
