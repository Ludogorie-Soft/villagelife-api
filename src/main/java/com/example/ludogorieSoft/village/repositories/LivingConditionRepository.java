package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.LivingCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingConditionRepository extends JpaRepository<LivingCondition,Long> {
    boolean existsByLivingConditionName(String condition);
}
