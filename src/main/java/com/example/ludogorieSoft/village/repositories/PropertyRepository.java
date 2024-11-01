package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);
    List<Property> findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long id);

    @Query(value = "SELECT DISTINCT p FROM Property p " +
            "WHERE (:propertyTypes IS NULL OR p.propertyType IN :propertyTypes) " +
            "AND (:propertyTransferType IS NULL OR p.propertyTransferType = :propertyTransferType) " +
            "AND (:minBuiltUpArea IS NULL OR p.buildUpArea >= :minBuiltUpArea) " +
            "AND (:maxBuiltUpArea IS NULL OR p.buildUpArea <= :maxBuiltUpArea) " +
            "AND (:minYardArea IS NULL OR p.yardArea >= :minYardArea) " +
            "AND (:maxYardArea IS NULL OR p.yardArea <= :maxYardArea) " +
            "AND (:minRoomsCount IS NULL OR p.roomsCount >= :minRoomsCount) " +
            "AND (:maxRoomsCount IS NULL OR p.roomsCount <= :maxRoomsCount) " +
            "AND (:minBathroomsCount IS NULL OR p.bathroomsCount >= :minBathroomsCount) " +
            "AND (:maxBathroomsCount IS NULL OR p.bathroomsCount <= :maxBathroomsCount) " +
            "AND (COALESCE(:heating) IS NULL OR p.heating IN :heating) " +
            "AND (COALESCE(:constructionTypes) IS NULL OR p.constructionType IN :constructionTypes) " +
            "AND (:minConstructionYear IS NULL OR p.constructionYear >= :minConstructionYear) " +
            "AND (:maxConstructionYear IS NULL OR p.constructionYear <= :maxConstructionYear) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (COALESCE(:ownershipTypes) IS NULL OR p.ownershipType IN :ownershipTypes)"
    )
    Page<Property> searchProperties(@Param("propertyTypes") List<String> propertyTypes,
                                  @Param("propertyTransferType") String propertyTransferType,
                                  @Param("minBuiltUpArea") Double minBuiltUpArea,
                                  @Param("maxBuiltUpArea") Double maxBuiltUpArea,
                                  @Param("minYardArea") Double minYardArea,
                                  @Param("maxYardArea") Double maxYardArea,
                                  @Param("minRoomsCount") Short minRoomsCount,
                                  @Param("maxRoomsCount") Short maxRoomsCount,
                                  @Param("minBathroomsCount") Short minBathroomsCount,
                                  @Param("maxBathroomsCount") Short maxBathroomsCount,
                                  @Param("heating") List<String> heating,
                                  @Param("constructionTypes") List<String> constructionTypes,
                                  @Param("minConstructionYear") Short minConstructionYear,
                                  @Param("maxConstructionYear") Short maxConstructionYear,
                                  @Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  @Param("ownershipTypes") List<String> ownershipTypes, Pageable pageable);
}
