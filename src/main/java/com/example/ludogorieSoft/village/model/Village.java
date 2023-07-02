package com.example.ludogorieSoft.village.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
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




    @OneToMany(mappedBy = "village")
    private List<ObjectVillage> objectVillages;


    @OneToMany(mappedBy = "village")
    private List<VillageLivingConditions> villageLivingConditions;




}
