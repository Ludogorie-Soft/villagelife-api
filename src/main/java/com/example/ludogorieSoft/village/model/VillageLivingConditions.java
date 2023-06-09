package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.Consents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Data
@Table(name = "village_living_conditions")
@NoArgsConstructor
@AllArgsConstructor
public class VillageLivingConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Village village;

    @ManyToOne
    private LivingCondition livingCondition;

    @Enumerated(EnumType.STRING)
    private Consents consents;

}
