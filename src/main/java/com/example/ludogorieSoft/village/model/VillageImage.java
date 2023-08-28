package com.example.ludogorieSoft.village.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "village_images")
public class VillageImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;
    @NotBlank
    private String imageName;
    private Boolean villageStatus;
    private LocalDateTime dateUpload;
    private LocalDateTime dateDeleted;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
