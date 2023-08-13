package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Distance;
import com.example.ludogorieSoft.village.model.ObjectVillage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ObjectVillageRepository extends JpaRepository<ObjectVillage,Long> {
    boolean existsByVillageIdAndObjectIdAndDistance(Long villageId, Long objectId, Distance distance);
    boolean existsByVillageIdAndObjectIdAndVillageStatus(Long villageId, Long objectId, boolean villageStatus);
    List<ObjectVillage> findByVillageIdAndObjectIdAndVillageStatus(Long villageId, Long objectId, boolean villageStatus);
    List<ObjectVillage> findByVillageId(Long villageId);
    List<ObjectVillage> findByVillageIdAndVillageStatus(Long villageId, boolean villageStatus);
    @Query("SELECT v FROM ObjectVillage v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND (v.dateDeleted IS NULL) " +
            "AND FUNCTION('DATE_FORMAT', v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<ObjectVillage> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, boolean villageStatus, String localDateTime);
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ObjectVillage v " +
            "WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND v.object.id = :objectId " +
            "AND (v.dateDeleted IS NULL) " +
            "AND FUNCTION('DATE_FORMAT', v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    boolean existsByVillageIdAndObjectIdAndVillageStatusAndDate(Long villageId, Long objectId, boolean villageStatus, String localDateTime);
    @Query("SELECT v FROM ObjectVillage v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND v.object.id = :objectId " +
            "AND (v.dateDeleted IS NULL) " +
            "AND FUNCTION('DATE_FORMAT', v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<ObjectVillage> findByVillageIdAndObjectIdAndVillageStatusAndDate(Long villageId, Long objectId, boolean villageStatus, String localDateTime);

}
