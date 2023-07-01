package com.example.ludogorieSoft.village.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "ground_categories")
public class GroundCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank
    @Column(name = "ground_category_name", unique = true, nullable = false)
    private String groundCategoryName;


}
