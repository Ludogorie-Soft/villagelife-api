package com.example.ludogorieSoft.village.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name ="populated_assertion" )
@AllArgsConstructor
@NoArgsConstructor
public class PopulatedAssertion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "populated_assertion_name",unique = true)
    private String populatedAssertionName;

}
