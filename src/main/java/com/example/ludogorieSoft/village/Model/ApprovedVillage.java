package com.example.ludogorieSoft.village.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "approved_villages")
@AllArgsConstructor
@NoArgsConstructor
public class ApprovedVillage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private Village village;
    @ManyToOne
    private Administrator admin;
    private Date dateApproved;

}
