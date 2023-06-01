package com.example.ludogorieSoft.village.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
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
    private String name;
    @OneToOne
    private Population population;
    private LocalDateTime dateUpload;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "admin_id" )
    private Administrator admin;
    private LocalDateTime dateApproved;

}
