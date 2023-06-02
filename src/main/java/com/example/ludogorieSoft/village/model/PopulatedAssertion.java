package com.example.ludogorieSoft.village.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name ="populated_assertion" )
public class PopulatedAssertion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String assertion;

}
