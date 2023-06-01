package com.example.ludogorieSoft.village.Model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "living_conditions")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LivingConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String conditions;

}
