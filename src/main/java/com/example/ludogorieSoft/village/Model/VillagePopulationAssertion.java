package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "village_population_assertion")
public class VillagePopulationAssertion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private Village villageID;
    @ManyToOne
    private PopulatedAssertion populatedAssertionID;
    @NotNull
    private String answer;


}
