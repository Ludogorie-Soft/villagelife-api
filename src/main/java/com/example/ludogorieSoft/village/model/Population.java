package com.example.ludogoriesoft.village.model;


import com.example.ludogoriesoft.village.enums.Children;
import com.example.ludogoriesoft.village.enums.Foreigners;
import com.example.ludogoriesoft.village.enums.NumberOfPopulation;
import com.example.ludogoriesoft.village.enums.Residents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
