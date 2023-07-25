package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.Consents;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "village_population_assertion")
public class VillagePopulationAssertion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;
    @ManyToOne
    @JoinColumn(name = "populated_assertion_id")
    private PopulatedAssertion populatedAssertionID;
    @Enumerated(EnumType.STRING)
    private Consents answer;
    private Boolean villageStatus;


}
