package com.example.ludogorieSoft.village.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "regions")
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank
    @Column(name = "region_name", unique = true, nullable = false)
    private String regionName;
    @NotBlank
    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
