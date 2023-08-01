package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillagePopulationAssertion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VillagePopulationAssertionRepository extends JpaRepository<VillagePopulationAssertion, Long>{
    List<VillagePopulationAssertion> findByVillageIdAndPopulatedAssertionIDIdAndVillageStatus(Long villageId, Long populatedAssertionId, Boolean villageStatus);
    List<VillagePopulationAssertion> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, Boolean villageStatus, LocalDateTime localDateTime);
}
