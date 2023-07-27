package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.Consents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "village_population_assertion")
@AllArgsConstructor
@NoArgsConstructor
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


}
