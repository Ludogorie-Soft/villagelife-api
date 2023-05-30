package com.example.ludogorieSoft.village.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ObjectsNearby {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


}
