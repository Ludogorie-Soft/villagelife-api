package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VillageLivingConditionRepository extends JpaRepository<VillageLivingConditions, Long> {
    List<VillageLivingConditions> findByVillageIdAndLivingConditionIdAndVillageStatus(Long villageId, Long livingConditionId, boolean villageStatus);
    boolean existsByVillageIdAndLivingConditionIdAndVillageStatus(Long villageId, Long livingConditionId, boolean villageStatus);
    @Query("SELECT v FROM VillageLivingConditions v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<VillageLivingConditions> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, boolean villageStatus, String localDateTime);
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END " +
            "FROM VillageLivingConditions v " +
            "WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND v.livingCondition.id = :livingConditionId " +
            "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    boolean existsByVillageIdAndLivingConditionIdAndVillageStatusAndDate(Long villageId, Long livingConditionId, boolean villageStatus,String localDateTime);
    @Query("SELECT v FROM VillageLivingConditions v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND v.livingCondition.id = :livingConditionId " +
            "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<VillageLivingConditions> findByVillageIdAndLivingConditionIdAndVillageStatusAndDate(Long villageId, Long livingConditionId, boolean villageStatus, String localDateTime) ;

}
