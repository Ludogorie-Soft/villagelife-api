package com.example.ludogorieSoft.village.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
    @NotBlank
    @Column(nullable = false)
    private String latinName;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    private LocalDateTime dateUpload;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AlternativeUser admin;
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

    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Population> populations;

    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inquiry> inquiries;

    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VillageVideo> villageVideos;

    private int approvedResponsesCount;

}
