package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "village_landscape")
public class VillageLandscape {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private Village villageID;
    @ManyToOne
    private Landscape landscape;

}
