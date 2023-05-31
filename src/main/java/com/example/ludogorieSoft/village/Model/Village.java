package com.example.ludogorieSoft.village.Model;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "village")
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
   // private Population population;
    private Date dateUpload;
    private boolean status;



}
