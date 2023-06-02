package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.LivingConditions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingConditionsRepositories extends JpaRepository<LivingConditions,Long> {
    boolean existsByLivingCondition(String condition);
}
