package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.ConstructionType;
import com.example.ludogorieSoft.village.enums.OwnershipType;
import com.example.ludogorieSoft.village.enums.PropertyCondition;
import com.example.ludogorieSoft.village.enums.PropertyTransferType;
import com.example.ludogorieSoft.village.enums.PropertyType;
import com.example.ludogorieSoft.village.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    List<Property> findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long id);

    @Query(value = "SELECT DISTINCT p FROM Property p " +
            "LEFT JOIN p.heating h " +
            "JOIN p.village v " +
            "JOIN v.region r " +
            "WHERE (COALESCE(:propertyTypes) IS NULL OR p.propertyType IN :propertyTypes) " +
            "AND (:propertyTransferType IS NULL OR p.propertyTransferType = :propertyTransferType) " +
            "AND (:minBuiltUpArea IS NULL OR p.buildUpArea >= :minBuiltUpArea) " +
            "AND (:maxBuiltUpArea IS NULL OR p.buildUpArea <= :maxBuiltUpArea) " +
            "AND (:minYardArea IS NULL OR p.yardArea >= :minYardArea) " +
            "AND (:maxYardArea IS NULL OR p.yardArea <= :maxYardArea) " +
            "AND (:minRoomsCount IS NULL OR p.roomsCount >= :minRoomsCount) " +
            "AND (:maxRoomsCount IS NULL OR p.roomsCount <= :maxRoomsCount) " +
            "AND (:minBathroomsCount IS NULL OR p.bathroomsCount >= :minBathroomsCount) " +
            "AND (:maxBathroomsCount IS NULL OR p.bathroomsCount <= :maxBathroomsCount) " +
            "AND (COALESCE(:heating) IS NULL OR h IN :heating) " +
            "AND (COALESCE(:constructionTypes) IS NULL OR p.constructionType IN :constructionTypes) " +
            "AND (COALESCE(:propertyConditions) IS NULL OR p.propertyCondition IN :propertyConditions)" +
            "AND (:minConstructionYear IS NULL OR p.constructionYear >= :minConstructionYear) " +
            "AND (:maxConstructionYear IS NULL OR p.constructionYear <= :maxConstructionYear) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (COALESCE(:ownershipTypes) IS NULL OR p.ownershipType IN :ownershipTypes)" +
            "AND (COALESCE(:villageName) IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :villageName, '%'))) " +
            "AND (COALESCE(:regionName) IS NULL OR LOWER(r.regionName) LIKE LOWER(CONCAT('%', :regionName, '%'))) " +
            "AND p.deactivatedAt IS NULL " +
            "AND p.deletedAt IS NULL"
    )
    Page<Property> searchProperties(@Param("propertyTypes") List<PropertyType> propertyTypes,
                                    @Param("propertyTransferType") PropertyTransferType propertyTransferType,
                                    @Param("minBuiltUpArea") Double minBuiltUpArea,
                                    @Param("maxBuiltUpArea") Double maxBuiltUpArea,
                                    @Param("minYardArea") Double minYardArea,
                                    @Param("maxYardArea") Double maxYardArea,
                                    @Param("minRoomsCount") Short minRoomsCount,
                                    @Param("maxRoomsCount") Short maxRoomsCount,
                                    @Param("minBathroomsCount") Short minBathroomsCount,
                                    @Param("maxBathroomsCount") Short maxBathroomsCount,
                                    @Param("heating") List<String> heating,
                                    @Param("constructionTypes") List<ConstructionType> constructionTypes,
                                    @Param("propertyConditions") List<PropertyCondition> propertyConditions,
                                    @Param("minConstructionYear") String minConstructionYear,
                                    @Param("maxConstructionYear") String maxConstructionYear,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    @Param("ownershipTypes") List<OwnershipType> ownershipTypes,
                                    @Param("villageName") String villageName,
                                    @Param("regionName") String regionName,
                                    Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = """
                UPDATE PropertyStats ps
                SET ps.seenInResults = ps.seenInResults + 1
                WHERE ps IN (
                    SELECT p.propertyStats
                    FROM Property p
                    LEFT JOIN p.heating h
                    JOIN p.village v
                    JOIN v.region r
                    WHERE (COALESCE(:propertyTypes) IS NULL OR p.propertyType IN :propertyTypes)
                    AND (:propertyTransferType IS NULL OR p.propertyTransferType = :propertyTransferType)
                    AND (:minBuiltUpArea IS NULL OR p.buildUpArea >= :minBuiltUpArea)
                    AND (:maxBuiltUpArea IS NULL OR p.buildUpArea <= :maxBuiltUpArea)
                    AND (:minYardArea IS NULL OR p.yardArea >= :minYardArea)
                    AND (:maxYardArea IS NULL OR p.yardArea <= :maxYardArea)
                    AND (:minRoomsCount IS NULL OR p.roomsCount >= :minRoomsCount)
                    AND (:maxRoomsCount IS NULL OR p.roomsCount <= :maxRoomsCount)
                    AND (:minBathroomsCount IS NULL OR p.bathroomsCount >= :minBathroomsCount)
                    AND (:maxBathroomsCount IS NULL OR p.bathroomsCount <= :maxBathroomsCount)
                    AND (COALESCE(:heating) IS NULL OR h IN :heating)
                    AND (COALESCE(:constructionTypes) IS NULL OR p.constructionType IN :constructionTypes)
                    AND (COALESCE(:propertyConditions) IS NULL OR p.propertyCondition IN :propertyConditions)
                    AND (:minConstructionYear IS NULL OR p.constructionYear >= :minConstructionYear)
                    AND (:maxConstructionYear IS NULL OR p.constructionYear <= :maxConstructionYear)
                    AND (:minPrice IS NULL OR p.price >= :minPrice)
                    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                    AND (COALESCE(:ownershipTypes) IS NULL OR p.ownershipType IN :ownershipTypes)
                    AND (COALESCE(:villageName) IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :villageName, '%')))
                    AND (COALESCE(:regionName) IS NULL OR LOWER(r.regionName) LIKE LOWER(CONCAT('%', :regionName, '%')))
                    AND p.deactivatedAt IS NULL
                    AND p.deletedAt IS NULL
                    AND p.propertyStats IS NOT NULL
                )
            """)
    void updateSeenInResultsForFilteredProperties(
            @Param("propertyTypes") List<PropertyType> propertyTypes,
            @Param("propertyTransferType") PropertyTransferType propertyTransferType,
            @Param("minBuiltUpArea") Double minBuiltUpArea,
            @Param("maxBuiltUpArea") Double maxBuiltUpArea,
            @Param("minYardArea") Double minYardArea,
            @Param("maxYardArea") Double maxYardArea,
            @Param("minRoomsCount") Short minRoomsCount,
            @Param("maxRoomsCount") Short maxRoomsCount,
            @Param("minBathroomsCount") Short minBathroomsCount,
            @Param("maxBathroomsCount") Short maxBathroomsCount,
            @Param("heating") List<String> heating,
            @Param("constructionTypes") List<ConstructionType> constructionTypes,
            @Param("propertyConditions") List<PropertyCondition> propertyConditions,
            @Param("minConstructionYear") String minConstructionYear,
            @Param("maxConstructionYear") String maxConstructionYear,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("ownershipTypes") List<OwnershipType> ownershipTypes,
            @Param("villageName") String villageName,
            @Param("regionName") String regionName
    );

}
