package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface VillageRepository extends JpaRepository<Village, Long> {

    List<Village> findByStatus(Boolean status);
    List<Village> findByStatus(Boolean status, Pageable page);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE v.status = 1 ORDER BY r.regionName ASC")
    List<Village> findAllApprovedVillages();

    @Query("SELECT v FROM Village v JOIN v.region r WHERE LOWER(v.name) LIKE %:keyword% AND v.status = 1 ORDER BY r.regionName ASC")
    List<Village> findByNameContainingIgnoreCaseOrderByRegionNameAsc(@Param("keyword") String keyword);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE LOWER(r.regionName) = :regionName AND v.status = 1 ORDER BY r.regionName ASC")
    List<Village> findByRegionName(@Param("regionName") String regionName);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE r.regionName = :regionName AND LOWER(v.name) LIKE %:keyword% AND v.status = 1 ORDER BY r.regionName ASC")
    List<Village> findByNameContainingIgnoreCaseAndRegionName(@Param("regionName") String regionName, @Param("keyword") String keyword);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE v.name = :villageName AND r.regionName = :regionName")
    Village findSingleVillageByNameAndRegionName(@Param("villageName") String villageName, @Param("regionName") String regionName);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN Population p ON v.id = p.village.id " +
            "JOIN v.region r " +
            "WHERE o.type IN :objectTypes " +
            "AND lc.livingConditionName IN :livingConditionNames " +
            "AND p.children = :childrenCount " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    List<Village> searchVillages(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                 @Param("livingConditionNames") List<String> livingConditionDTOS,
                                 @Param("childrenCount") Children children);


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
    List<Village> searchVillagesByLivingConditionAndChildren(
            @Param("livingConditionNames") List<String> livingConditionDTOS,
            @Param("childrenCount") Children children);


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
    List<Village> searchVillagesByObjectAndChildren(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                                    @Param("childrenCount") Children children);


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
    List<Village> searchVillagesByObjectAndLivingCondition(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                                           @Param("livingConditionNames") List<String> livingConditionDTOS);


    @Query("SELECT DISTINCT v FROM Village v " +
            "JOIN Population p ON v.id = p.village " +
            "JOIN v.region r " +
            "WHERE p.children = :childrenCount " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    List<Village> searchVillagesByChildrenCount(@Param("childrenCount") Children children);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.region r " +
            "WHERE o.type IN :objectTypes " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    List<Village> searchVillagesByObject(@Param("objectTypes") List<String> objectAroundVillageDTOS);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN v.region r " +
            "WHERE lc.livingConditionName IN :livingConditionNames " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    List<Village> searchVillagesByLivingCondition(@Param("livingConditionNames") List<String> livingConditionDTOS);
    @Query("SELECT DISTINCT v FROM Village v " +
            "JOIN v.ethnicityVillages ev " +
            "WHERE ev.dateDeleted IS NOT NULL " +
            "ORDER BY ev.villageStatus DESC, ev.dateDeleted ASC")
    List<Village> findAllVillagesWithRejectedResponses();
    List<Village> findByName(String name);

}
