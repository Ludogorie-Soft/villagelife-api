package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class LivingConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private int infrastructure;
    private int accessibility;
    private int hygiene;
    private int publicTransport;
    private int powerGrid;
    private int waterQuality;
    private int cellularRange;
    private int internetRange;
    private int TVRange;

}
