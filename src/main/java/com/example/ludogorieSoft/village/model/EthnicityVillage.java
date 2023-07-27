package com.example.ludogorieSoft.village.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "village_ethnicities")
@AllArgsConstructor
@NoArgsConstructor
public class EthnicityVillage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;
    @ManyToOne
    @JoinColumn(name = "ethnicity_id")
    private Ethnicity ethnicity;
    private Boolean villageStatus;
    private LocalDateTime dateUpload;
}
