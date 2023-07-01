package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VillageRepository extends JpaRepository<Village, Long> {
   // @Query("SELECT v, p FROM Village v JOIN v.population p")
   @Query("SELECT v, p, a FROM Village v JOIN v.population p JOIN v.admin a")
    List<Object[]> findAllVillagesWithPopulation();
}
