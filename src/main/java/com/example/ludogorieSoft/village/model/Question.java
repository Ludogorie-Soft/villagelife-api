package com.example.ludogorieSoft.village.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "question_name")
    private String questionName;

}
