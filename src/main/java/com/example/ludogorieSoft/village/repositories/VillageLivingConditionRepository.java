package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VillageLivingConditionRepository extends JpaRepository<VillageLivingConditions, Long> {
    List<VillageLivingConditions> findByVillageIdAndLivingConditionId(Long villageId, Long livingConditionId);
    boolean existsByVillageIdAndLivingConditionId(Long villageId, Long livingConditionId);
}
