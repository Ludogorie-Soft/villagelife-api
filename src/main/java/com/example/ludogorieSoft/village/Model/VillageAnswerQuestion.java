package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class VillageAnswerQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private Village villageID;
    @ManyToOne
    private Questions questionsID;
    private String answer;


}
