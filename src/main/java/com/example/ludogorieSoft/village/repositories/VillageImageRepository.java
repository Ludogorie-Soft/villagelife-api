package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VillageImageRepository extends JpaRepository<VillageImage, Long> {
    List<VillageImage> findByVillageIdAndVillageStatus(Long villageId, boolean villageStatus);
    @Query("SELECT v FROM VillageImage v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<VillageImage> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, boolean villageStatus, String localDateTime);
}
