package com.example.ludogorieSoft.village.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "landscapes")
@Data
public class Landscape {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String landscapeName;
}
