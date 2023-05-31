package com.example.ludogorieSoft.village.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "village_objects")
@AllArgsConstructor
public class ObjectVillage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private Village village;
    @ManyToOne
    private ObjectAroundVillage objectAroundVillage;
}
