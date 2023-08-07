package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillagePopulationAssertion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VillagePopulationAssertionRepository extends JpaRepository<VillagePopulationAssertion, Long>{
    List<VillagePopulationAssertion> findByVillageIdAndPopulatedAssertionIDIdAndVillageStatus(Long villageId, Long populatedAssertionId, Boolean villageStatus);
@Query("SELECT v FROM VillagePopulationAssertion v WHERE v.village.id = :villageId " +
        "AND v.villageStatus = :villageStatus " +
        "AND (v.dateDeleted IS NULL) " +
        "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
List<VillagePopulationAssertion> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, Boolean villageStatus, String localDateTime);
    @Query("SELECT v FROM VillagePopulationAssertion v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND v.populatedAssertionID.id = :populatedAssertionId " +
            "AND (v.dateDeleted IS NULL) " +
            "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<VillagePopulationAssertion> findByVillageIdAndPopulatedAssertionIDIdAndVillageStatusAndDateUpload(Long villageId, Long populatedAssertionId, Boolean villageStatus, String localDateTime);

}
