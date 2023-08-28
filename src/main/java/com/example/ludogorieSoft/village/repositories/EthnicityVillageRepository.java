package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.EthnicityVillage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EthnicityVillageRepository extends JpaRepository<EthnicityVillage, Long> {
    boolean existsByEthnicityIdAndVillageId(Long ethnicityId, Long villageId);
    @Query("SELECT e FROM EthnicityVillage e WHERE e.village.id = :villageId " +
            "AND e.villageStatus = :status " +
            "AND DATE_FORMAT(e.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<EthnicityVillage> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, boolean status, String localDateTime);
}
