package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.model.VillagePopulationAssertion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VillagePopulationAssertionRepository extends JpaRepository<VillagePopulationAssertion, Long>{
    boolean existsByVillageIdAndPopulatedAssertionIDIdAndAnswer(Long villageId, Long populatedAssertionId, Consents answer);
}
