package com.example.ludogoriesoft.village.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    @Column(name = "living_condition_name", unique = true, nullable = false)
    private String livingConditionName;

}
