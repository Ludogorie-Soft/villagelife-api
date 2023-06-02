package com.example.ludogorieSoft.village.Model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "living_conditions")
@NoArgsConstructor
@AllArgsConstructor
public class LivingConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String livingCondition;

}
