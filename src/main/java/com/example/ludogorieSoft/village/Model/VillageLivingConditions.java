package com.example.ludogorieSoft.village.Model;

import com.example.ludogorieSoft.village.enums.Consents;

import javax.persistence.*;

public class VillageLivingConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @Column(name = "village_id")
    private Village village;
    private Consents consents;

}
