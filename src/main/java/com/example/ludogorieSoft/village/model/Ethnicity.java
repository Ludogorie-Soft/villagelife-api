package com.example.ludogoriesoft.village.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "ethnicities")
@AllArgsConstructor
@NoArgsConstructor
public class Ethnicity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank
    @Column(unique = true, nullable = false)
    private String ethnicityName;
}
