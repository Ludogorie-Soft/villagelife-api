package com.example.ludogorieSoft.village.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name ="questions" )
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String question;

}
