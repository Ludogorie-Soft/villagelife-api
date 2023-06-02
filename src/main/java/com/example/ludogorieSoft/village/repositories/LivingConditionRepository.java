package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.LivingCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingConditionRepository extends JpaRepository<LivingCondition,Long> {
    boolean existsByLivingCondition(String condition);
}
