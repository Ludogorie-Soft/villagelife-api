package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectAroundVillageRepository extends JpaRepository<ObjectAroundVillage,Long> {
    boolean existsByType(String type);
}
