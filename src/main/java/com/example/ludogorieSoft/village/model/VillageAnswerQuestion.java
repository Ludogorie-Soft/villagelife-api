package com.example.ludogorieSoft.village.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class VillageAnswerQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Questions questions;
    private String answer;


}
