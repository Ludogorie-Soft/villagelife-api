package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VillageGroundCategoryRepository extends JpaRepository<VillageGroundCategory, Long> {
    VillageGroundCategory findByVillageId(Long villageId);
    @Query("SELECT vgc FROM VillageGroundCategory vgc WHERE vgc.village.id = :villageId " +
            "AND vgc.villageStatus = :status " +
            "AND FUNCTION('DATE_FORMAT', vgc.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<VillageGroundCategory> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, boolean status, String localDateTime);

    boolean existsByGroundCategoryIdAndVillageId(Long groundCategoryId, Long villageId);
}
