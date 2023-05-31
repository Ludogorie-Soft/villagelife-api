package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Village_Answer_Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village villageID;
    @ManyToOne
    @JoinColumn(name = "questions_id")
    private Questions questionsID;
    private String answer;


}
