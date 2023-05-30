package com.example.ludogorieSoft.village.Model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Data
@Table(name ="population" )
public class Population {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private int populationNumber;
    private int under50age;
    private int childrenNumber;
    private boolean foreigners;
    private String ethnicComposition;






}
