package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PopulationRepository extends JpaRepository<Population, Long> {

    @Query("SELECT p FROM Population p " +
            "JOIN Village v ON v.id = p.village.id " +
            "JOIN v.region r " +
            "WHERE v.name = :villageName AND r.regionName = :regionName")
    Population findByVillageNameAndRegionName(@Param("villageName") String villageName, @Param("regionName") String regionName);

}
