package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PropertyStatsRepository extends JpaRepository<PropertyStats, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE PropertyStats ps SET ps.views = ps.views + 1 " +
            "WHERE ps IN (SELECT p.propertyStats FROM Property p WHERE p = :property AND p.propertyStats IS NOT NULL)")
    void incrementViewsForProperty(@Param("property") Property property);

    @Transactional
    @Modifying
    @Query("UPDATE PropertyStats ps SET ps.saves = ps.saves + 1 " +
            "WHERE ps IN (SELECT p.propertyStats FROM Property p WHERE p = :property AND p.propertyStats IS NOT NULL)")
    void incrementSavesForProperty(@Param("property") Property property);

    @Transactional
    @Modifying
    @Query("UPDATE PropertyStats ps SET ps.saves = ps.saves - 1 " +
            "WHERE ps IN (SELECT p.propertyStats FROM Property p WHERE p = :property AND p.propertyStats IS NOT NULL) " +
            "AND ps.saves > 0")
    void decrementSavesForProperty(@Param("property") Property property);

    @Transactional
    @Modifying
    @Query("UPDATE PropertyStats ps SET ps.seenInResults = ps.seenInResults + 1 " +
            "WHERE ps IN (SELECT p.propertyStats FROM Property p WHERE p IN :properties AND p.propertyStats IS NOT NULL)")
    void incrementSeenInResultsForProperties(@Param("properties") List<Property> properties);
}
