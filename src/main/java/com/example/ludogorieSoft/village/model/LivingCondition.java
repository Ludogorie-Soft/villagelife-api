package com.example.ludogorieSoft.village.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "living_conditions")
@NoArgsConstructor
@AllArgsConstructor
public class LivingCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String livingCondition;

}
