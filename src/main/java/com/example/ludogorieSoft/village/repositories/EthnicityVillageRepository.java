package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.EthnicityVillage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EthnicityVillageRepository extends JpaRepository<EthnicityVillage, Long> {
    boolean existsByEthnicityIdAndVillageId(Long ethnicityId, Long villageId);
    List<EthnicityVillage> findByVillageIdAndVillageStatusAndDateUpload(Long id, boolean status, LocalDateTime localDateTime);

    List<EthnicityVillage> findByVillageIdAndVillageStatus(Long id, boolean status);

}
