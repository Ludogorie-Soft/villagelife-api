package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.Distance;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "village_objects")
@AllArgsConstructor
@NoArgsConstructor
public class ObjectVillage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private Village village;
    @ManyToOne
    private ObjectAroundVillage object;
    @Enumerated(EnumType.STRING)
    private Distance distance;
    private Boolean villageStatus;
    @CreationTimestamp
    private LocalDateTime dateUpload;
}
