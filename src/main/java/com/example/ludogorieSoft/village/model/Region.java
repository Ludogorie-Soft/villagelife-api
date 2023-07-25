package com.example.ludogorieSoft.village.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
}
