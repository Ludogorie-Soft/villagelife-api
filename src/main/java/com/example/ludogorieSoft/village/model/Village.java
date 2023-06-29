package com.example.ludogorieSoft.village.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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

}
