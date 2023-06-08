package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.Landscape;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandscapeRepository extends JpaRepository<Landscape, Long> {
    boolean existsByLandscapeName(String landscapeName);
}
