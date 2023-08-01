package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VillageGroundCategoryRepository extends JpaRepository<VillageGroundCategory, Long> {
    VillageGroundCategory findByVillageId(Long villageId);
    List<VillageGroundCategory> findByVillageIdAndVillageStatusAndDateUpload(Long id, boolean ststus, LocalDateTime localDateTime);
}
