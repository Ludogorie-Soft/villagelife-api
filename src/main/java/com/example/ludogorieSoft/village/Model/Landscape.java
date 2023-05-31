package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "landscapes")
@Data
public class Landscape {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String landscapeName;
}
