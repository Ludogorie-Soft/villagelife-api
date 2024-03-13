package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.model.Village;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.util.List;


public interface VillageRepository extends JpaRepository<Village, Long> {

    Page<Village> findByStatus(Boolean status, Pageable page);


    @Query("SELECT v FROM Village v JOIN v.region r WHERE v.status = 1 ORDER BY r.regionName ASC")
    Page<Village> findAllApprovedVillages(Pageable pageable);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE LOWER(v.name) LIKE %:keyword% AND v.status = 1 ORDER BY r.regionName ASC")
    Page<Village> findByNameContainingIgnoreCaseOrderByRegionNameAsc(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE LOWER(r.regionName) = :regionName AND v.status = 1 ORDER BY r.regionName ASC")
    Page<Village> findByRegionName(@Param("regionName") String regionName, Pageable pageable);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE r.regionName = :regionName AND LOWER(v.name) LIKE %:keyword% AND v.status = 1 ORDER BY r.regionName ASC")
    Page<Village> findByNameContainingIgnoreCaseAndRegionName(@Param("regionName") String regionName, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE v.name = :villageName AND r.regionName = :regionName")
    Village findSingleVillageByNameAndRegionName(@Param("villageName") String villageName, @Param("regionName") String regionName);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "LEFT JOIN v.objectVillages ov " +
            "LEFT JOIN ov.object o " +
            "LEFT JOIN v.villageLivingConditions vl " +
            "LEFT JOIN vl.livingCondition lc " +
            "LEFT JOIN Population p ON v.id = p.village.id " +
            "LEFT JOIN v.region r " +
            "WHERE (:regions IS NULL OR r.regionName =:regions) " +
            "AND (:villageName IS NULL OR v.name LIKE %:villageName%) " +
            "AND (coalesce(:objectTypes) IS NULL OR o.type IN (:objectTypes) AND ov.distance = 'IN_THE_VILLAGE' AND vl.consents = 'COMPLETELY_AGREED') " +
            "AND (coalesce(:livingConditionNames) IS NULL OR lc.livingConditionName IN (:livingConditionNames) AND ov.distance = 'IN_THE_VILLAGE' AND vl.consents = 'COMPLETELY_AGREED') " +
            "AND (:childrenCount IS NULL OR p.children = :childrenCount AND ov.distance = 'IN_THE_VILLAGE' AND vl.consents = 'COMPLETELY_AGREED') " +
            "AND status=1"
    )
    Page<Village> searchVillages(
            @Param("regions") String region,
            @Param("villageName") String villageName,
            @Param("objectTypes") List<String> objectTypes,
            @Param("livingConditionNames") List<String> livingConditionNames,
            @Param("childrenCount") Children children,
            Pageable pageable);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN Population p ON v.id = p.village.id " +
            "JOIN v.region r " +
            "WHERE lc.livingConditionName IN :livingConditionNames " +
            "AND p.children = :childrenCount " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    Page<Village> searchVillagesByLivingConditionAndChildren(
            @Param("livingConditionNames") List<String> livingConditionDTOS,
            @Param("childrenCount") Children children, Pageable pageable);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN Population p ON v.id = p.village.id " +
            "JOIN v.region r " +
            "WHERE o.type IN :objectTypes " +
            "AND p.children = :childrenCount " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    Page<Village> searchVillagesByObjectAndChildren(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                                    @Param("childrenCount") Children children, Pageable pageable);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN v.region r " +
            "WHERE o.type IN :objectTypes " +
            "AND lc.livingConditionName IN :livingConditionNames " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    Page<Village> searchVillagesByObjectAndLivingCondition(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                                           @Param("livingConditionNames") List<String> livingConditionDTOS,
                                                           Pageable pageable);


    @Query("SELECT DISTINCT v FROM Village v " +
            "JOIN Population p ON v.id = p.village " +
            "JOIN v.region r " +
            "WHERE p.children = :childrenCount " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    Page<Village> searchVillagesByChildrenCount(@Param("childrenCount") Children children, Pageable pageable);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.region r " +
            "WHERE o.type IN :objectTypes " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    Page<Village> searchVillagesByObject(@Param("objectTypes") List<String> objectAroundVillageDTOS, Pageable pageable);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN v.region r " +
            "WHERE lc.livingConditionName IN :livingConditionNames " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    Page<Village> searchVillagesByLivingCondition(@Param("livingConditionNames") List<String> livingConditionDTOS, Pageable pageable);

    @Query("SELECT DISTINCT v FROM Village v " +
            "JOIN v.ethnicityVillages ev " +
            "WHERE ev.dateDeleted IS NOT NULL " +
            "ORDER BY ev.villageStatus DESC, ev.dateDeleted ASC")
    List<Village> findAllVillagesWithRejectedResponses();

    List<Village> findByName(String name);

}
