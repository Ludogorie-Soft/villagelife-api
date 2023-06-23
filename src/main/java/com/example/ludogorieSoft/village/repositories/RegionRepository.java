package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    boolean existsByRegionName(String regionName);
    List<Region> findAllByOrderByIdAsc();
    Region findByRegionName(String name);

}
