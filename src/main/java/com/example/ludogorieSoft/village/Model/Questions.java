package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name ="questions" )
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String question;

}
