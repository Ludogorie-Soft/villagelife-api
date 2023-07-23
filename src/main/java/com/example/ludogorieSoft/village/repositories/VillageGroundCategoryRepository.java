package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VillageGroundCategoryRepository extends JpaRepository<VillageGroundCategory, Long> {
    VillageGroundCategory findByVillageId(Long villageId);
}
