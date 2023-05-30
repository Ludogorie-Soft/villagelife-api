package com.example.ludogorieSoft.village.Model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class PopulationCharacteristics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private int friendliness;
    private int communicative;
    private int cautious;
    private int helpful;




}
