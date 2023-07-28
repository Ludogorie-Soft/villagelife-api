package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Distance;
import com.example.ludogorieSoft.village.model.ObjectVillage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjectVillageRepository extends JpaRepository<ObjectVillage,Long> {
    boolean existsByVillageIdAndObjectIdAndDistance(Long villageId, Long objectId, Distance distance);
    boolean existsByVillageIdAndObjectIdAndVillageStatus(Long villageId, Long objectId, boolean villageStatus);
    List<ObjectVillage> findByVillageIdAndObjectIdAndVillageStatus(Long villageId, Long objectId, boolean villageStatus);
    List<ObjectVillage> findByVillageId(Long villageId);
    List<ObjectVillage> findByVillageIdAndVillageStatus(Long villageId, boolean villageStatus);

}
