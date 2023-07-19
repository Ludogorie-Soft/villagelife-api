package com.example.ludogorieSoft.village.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "villages")
@NoArgsConstructor
@AllArgsConstructor
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    @Min(0)
    @NotNull
    private int populationCount;
    @OneToOne
    private Population population;
    @CreationTimestamp
    private LocalDateTime dateUpload;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Administrator admin;
    @CreationTimestamp
    private LocalDateTime dateApproved;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectVillage> objectVillages;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VillageLivingConditions> villageLivingConditions;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VillageAnswerQuestion> villageAnswerQuestions;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EthnicityVillage> ethnicityVillages;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VillageGroundCategory> villageGroundCategories;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VillageImage> villageImages;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VillageLandscape> villageLandscapes;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VillagePopulationAssertion> villagePopulationAssertions;




}
