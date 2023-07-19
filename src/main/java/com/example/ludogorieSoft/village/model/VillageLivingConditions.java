package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.Consents;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "village_living_conditions")
@NoArgsConstructor
@AllArgsConstructor
public class VillageLivingConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Village village;

    @ManyToOne
    private LivingCondition livingCondition;

    @Enumerated(EnumType.STRING)
    private Consents consents;

    private Boolean villageStatus;
    private LocalDateTime dateUpload;

}
