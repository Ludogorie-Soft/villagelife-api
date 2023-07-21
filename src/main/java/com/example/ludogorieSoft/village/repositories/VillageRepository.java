package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface VillageRepository extends JpaRepository<Village, Long> {
    @Query("SELECT v FROM Village v WHERE v.status = 1")
    List<Village> findAllApprovedVillages();

    @Query("SELECT v FROM Village v JOIN v.region r WHERE LOWER(v.name) LIKE %:keyword% AND v.status = 1 ORDER BY r.regionName ASC")
    List<Village> findByNameContainingIgnoreCaseOrderByRegionNameAsc(@Param("keyword") String keyword);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE LOWER(r.regionName) = :regionName AND v.status = 1")
    List<Village> findByRegionName(@Param("regionName") String regionName);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE r.regionName = :regionName AND LOWER(v.name) LIKE %:keyword% AND v.status = 1")
    List<Village> findByNameContainingIgnoreCaseAndRegionName(@Param("regionName") String regionName, @Param("keyword") String keyword);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN v.population p " +
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
            "JOIN v.population p " +
            "JOIN v.region r " +
            "WHERE lc.livingConditionName IN :livingConditionNames " +
            "AND p.children = :childrenCount " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "AND v.status = 1 " +
            "GROUP BY v.name " +
            "ORDER BY r.regionName ASC")
    List<Village> searchVillagesByLivingConditionAndChildren(@Param("livingConditionNames") List<String> livingConditionDTOS,
                                                             @Param("childrenCount") Children children);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.population p " +
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


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.population p " +
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

    @Query("SELECT v, r, a FROM Village v JOIN v.region r JOIN v.admin a")
    List<Object[]> findAllVillagesWithPopulation();
}
