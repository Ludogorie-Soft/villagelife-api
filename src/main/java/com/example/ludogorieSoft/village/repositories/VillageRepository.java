package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.model.Village;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface VillageRepository extends JpaRepository<Village, Long> {

    Page<Village> findByStatus(Boolean status, Pageable page);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE v.name = :villageName AND r.regionName = :regionName")
    Village findSingleVillageByNameAndRegionName(@Param("villageName") String villageName, @Param("regionName") String regionName);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE v.name = :villageName AND r.name = :regionName")
    List<Village> findSingleVillageByNameAndRegionName_forUpload(@Param("villageName") String villageName, @Param("regionName") String regionName);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "LEFT JOIN v.objectVillages ov " +
            "LEFT JOIN ov.object o " +
            "LEFT JOIN v.villageLivingConditions vl " +
            "LEFT JOIN vl.livingCondition lc " +
            "LEFT JOIN Population p ON v.id = p.village.id " +
            "LEFT JOIN v.region r " +
            "WHERE (:regions IS NULL OR :regions = '' OR r.regionName = :regions) " +
            "AND (:villageName IS NULL OR :villageName = '' OR (v.name LIKE %:villageName% OR v.latinName LIKE %:villageName%)) " +
            "AND (:objectTypes IS NULL OR :objectTypes = '' OR o.type IN (:objectTypes)) " +
            "AND (:livingConditionNames IS NULL OR :livingConditionNames = '' OR lc.livingConditionName IN (:livingConditionNames)) " +
            "AND (:childrenCount IS NULL OR p.children = :childrenCount) " +
            "AND (ov.distance = 'IN_THE_VILLAGE' OR ov.distance IS NULL) " +
            "AND (vl.consents = 'COMPLETELY_AGREED' OR vl.consents IS NULL) " +
            "AND v.status = true")
    Page<Village> searchVillages(
            @Param("regions") String region,
            @Param("villageName") String villageName,
            @Param("objectTypes") List<String> objectTypes,
            @Param("livingConditionNames") List<String> livingConditionNames,
            @Param("childrenCount") Children children,
            Pageable pageable);


    @Query("SELECT DISTINCT v FROM Village v " +
            "JOIN v.ethnicityVillages ev " +
            "WHERE ev.dateDeleted IS NOT NULL " +
            "ORDER BY ev.villageStatus DESC, ev.dateDeleted ASC")
    List<Village> findAllVillagesWithRejectedResponses();

    List<Village> findByName(String name);

    @Query("SELECT v.id FROM Village v WHERE v.status = ?1")
    List<Long> findAllApprovedVillageIdsByStatus(boolean status);
}
