package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VillageLivingConditionRepository extends JpaRepository<VillageLivingConditions, Long> {
    boolean existsByVillageIdAndLivingConditionIdAndConsents(Long villageId, Long livingConditionId, Consents consent);
}
