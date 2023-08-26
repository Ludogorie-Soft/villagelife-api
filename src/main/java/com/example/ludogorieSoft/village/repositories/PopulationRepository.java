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

    @Query("SELECT AVG(p.populationCount) FROM Population p WHERE p.village.id = :villageId " +
            "AND p.villageStatus = :villageStatus " +
            "AND DATE_FORMAT(p.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    double getAveragePopulationCountByVillageIdAndStatusAndDate(@Param("villageId") Long villageId,
                                                                @Param("villageStatus") Boolean villageStatus,
                                                                @Param("localDateTime") String localDateTime);



}
