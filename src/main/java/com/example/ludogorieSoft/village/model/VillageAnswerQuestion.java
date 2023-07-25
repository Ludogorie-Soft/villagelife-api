package com.example.ludogorieSoft.village.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Question question;
    private String answer;
    private Boolean villageStatus;
}
