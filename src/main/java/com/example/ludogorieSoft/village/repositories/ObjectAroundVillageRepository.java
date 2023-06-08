package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.ObjectAroundVillage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectAroundVillageRepository extends JpaRepository<ObjectAroundVillage,Long> {
    boolean existsByType(String type);
}
