package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {
    @Query("SELECT v FROM Village v WHERE v.name LIKE %:keyword%")
    List<Village> findByName(@Param("keyword") String keyword);


    @Query("SELECT v FROM Village v JOIN v.region r WHERE r.regionName = :regionName")
    List<Village> findByRegionName(@Param("regionName") String regionName);


    @Query("SELECT v FROM Village v JOIN v.region r WHERE r.regionName = :regionName AND v.name LIKE %:keyword%")
    List<Village> findByNameAndRegionName(@Param("regionName") String regionName, @Param("keyword") String keyword);

    @Query("SELECT v FROM Village v JOIN v.region r WHERE v.name = :villageName AND r.regionName = :regionName")
    Village findSingleVillageByNameAndRegionName(@Param("villageName") String villageName, @Param("regionName") String regionName);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN v.population p " +
            "WHERE o.type IN :objectTypes " +
            "AND lc.livingConditionName IN :livingConditionNames " +
            "AND p.children = :childrenCount " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "GROUP BY v.name")
    List<Village> searchVillages(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                 @Param("livingConditionNames") List<String> livingConditionDTOS,
                                 @Param("childrenCount") Children children);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN v.population p " +
            "WHERE lc.livingConditionName IN :livingConditionNames " +
            "AND p.children = :childrenCount " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "GROUP BY v.name")
    List<Village> searchVillagesByLivingConditionAndChildren(@Param("livingConditionNames") List<String> livingConditionDTOS,
                                                             @Param("childrenCount") Children children);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.population p " +
            "WHERE o.type IN :objectTypes " +
            "AND p.children = :childrenCount " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "GROUP BY v.name")
    List<Village> searchVillagesByObjectAndChildren(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                                    @Param("childrenCount") Children children);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "WHERE o.type IN :objectTypes " +
            "AND lc.livingConditionName IN :livingConditionNames " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "GROUP BY v.name")
    List<Village> searchVillagesByObjectAndLivingCondition(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                                           @Param("livingConditionNames") List<String> livingConditionDTOS);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.population p " +
            "WHERE p.children = :childrenCount " +
            "GROUP BY v.name")
    List<Village> searchVillagesByChildrenCount(@Param("childrenCount") Children children);


    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "WHERE o.type IN :objectTypes " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "GROUP BY v.name")
    List<Village> searchVillagesByObject(@Param("objectTypes") List<String> objectAroundVillageDTOS);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "WHERE lc.livingConditionName IN :livingConditionNames " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "GROUP BY v.name")
    List<Village> searchVillagesByLivingCondition(@Param("livingConditionNames") List<String> livingConditionDTOS);
}
