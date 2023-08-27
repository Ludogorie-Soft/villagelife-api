package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PopulationRepository extends JpaRepository<Population, Long> {

    @Query("SELECT p FROM Population p " +
            "JOIN Village v ON v.id = p.village.id " +
            "JOIN v.region r " +
            "WHERE v.name = :villageName AND r.regionName = :regionName")
    Population findByVillageNameAndRegionName(@Param("villageName") String villageName, @Param("regionName") String regionName);

    @Query("SELECT AVG(p.populationCount) FROM Population p WHERE p.village.id = :villageId AND p.villageStatus = true AND p.dateDeleted IS NULL")
    double getAveragePopulationCountByVillageId(Long villageId);

    @Query("SELECT COUNT(p), p.foreigners FROM Population p " +
            "WHERE p.village.id = :villageId " +
            "AND p.villageStatus = true " +
            "AND p.dateDeleted IS NULL " +
            "GROUP BY p.foreigners " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> countForeignersByVillageIdAndStatusTrueOrderedByCountDesc(@Param("villageId") Long villageId);

    @Query("SELECT COUNT(p), p.children FROM Population p " +
            "WHERE p.village.id = :villageId " +
            "AND p.villageStatus = true " +
            "AND p.dateDeleted IS NULL " +
            "GROUP BY p.children " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> countChildrenByVillageIdAndStatusTrueOrderedByCountDesc(@Param("villageId") Long villageId);

    @Query("SELECT COUNT(p), p.residents FROM Population p " +
            "WHERE p.village.id = :villageId " +
            "AND p.villageStatus = true " +
            "AND p.dateDeleted IS NULL " +
            "GROUP BY p.residents " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> countResidentsByVillageIdAndStatusTrueOrderedByCountDesc(@Param("villageId") Long villageId);

    @Query("SELECT p FROM Population p " +
            "WHERE p.village.id = :villageId " +
            "AND DATE_FORMAT(p.dateUpload, '%Y-%m-%d %H:%i:%s') = :dateUpload " +
            "AND p.villageStatus = :villageStatus")
    Population findPopulationsByVillageIdAndDateUploadAndStatus(
            @Param("villageId") Long villageId,
            @Param("dateUpload") String dateUpload,
            @Param("villageStatus") Boolean villageStatus
    );

    @Query("SELECT p FROM Population p WHERE p.village.id = :id " +
            "AND p.villageStatus = :status "+
            "AND p.dateDeleted != NULL " )
    List<Population> findByVillageIdAndVillageStatusAndDateDeleteNotNull(Long id, boolean status);

    @Query("SELECT p FROM Population p WHERE p.village.id = :id " +
            "AND p.villageStatus = :status "+
            "AND p.dateDeleted = NULL " )
    List<Population> findByVillageIdAndVillageStatus(Long id, boolean status);
}
