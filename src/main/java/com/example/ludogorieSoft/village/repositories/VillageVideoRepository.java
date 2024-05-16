package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VillageVideoRepository extends JpaRepository<VillageVideo, Long> {
    List<VillageVideo> findByVillageIdAndVillageStatusAndDateDeletedIsNull(Long villageId, boolean villageStatus);
    @Query("SELECT v FROM VillageImage v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<VillageVideo> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, boolean villageStatus, String localDateTime);

    List<VillageVideo> findByVillageId(Long villageId);
    List<VillageVideo> findByVillageIdAndStatusTrue(Long villageId);
}
