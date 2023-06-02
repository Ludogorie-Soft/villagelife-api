package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Landscape;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandscapeRepository extends JpaRepository<Landscape, Long> {
    boolean existsByLandscapeName(String landscapeName);
}
