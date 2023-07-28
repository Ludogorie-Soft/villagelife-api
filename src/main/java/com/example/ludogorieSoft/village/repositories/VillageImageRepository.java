package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VillageImageRepository extends JpaRepository<VillageImage, Long> {
    List<VillageImage> findByVillageIdAndVillageStatus(Long villageId, boolean villageStatus);
}
