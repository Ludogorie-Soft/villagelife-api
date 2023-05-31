package com.example.ludogorieSoft.village.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "objects")
@AllArgsConstructor
public class Object {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String type;
}
