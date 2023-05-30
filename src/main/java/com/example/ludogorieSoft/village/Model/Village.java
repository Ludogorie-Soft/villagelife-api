package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "village")
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private int populationNumber;
    private int crimeRate;
    private int livingConditionsNumber;
    private int EcoFriendliness;
    private int soilFertility;
    private boolean active;
    @OneToOne
    @JoinColumn(name = "population_id")
    private Population populationID;
    @OneToOne
    @JoinColumn(name = "populationCharacteristics_id")
    private PopulationCharacteristics populationCharacteristics;
    @OneToOne
    @JoinColumn(name = "livingConditions_id")
    private LivingConditions livingConditions;
    @OneToOne
    @JoinColumn(name = "objectsNearby_id")
    private ObjectsNearby objectsNearby;
    @OneToOne
    @JoinColumn(name = "landscape_id")
    private Landscape landscape;
}
