package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.enums.PropertyTransferType;
import com.example.ludogorieSoft.village.enums.PropertyType;
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

//    @Query(value = "SELECT DISTINCT p FROM Property p " +
//            "WHERE (:propertyTypes IS NULL OR p.propertyType IN :propertyTypes) " +
//            "AND (:propertyTransferType IS NULL OR p.propertyTransferType = :propertyTransferType) " +
//            "AND (:minBuiltUpArea IS NULL OR p.buildUpArea >= :minBuiltUpArea) " +
//            "AND (:maxBuiltUpArea IS NULL OR p.buildUpArea <= :maxBuiltUpArea) " +
//            "AND (:minYardArea IS NULL OR p.yardArea >= :minYardArea) " +
//            "AND (:maxYardArea IS NULL OR p.yardArea <= :maxYardArea) " +
//            "AND (:minRoomsCount IS NULL OR p.roomsCount >= :minRoomsCount) " +
//            "AND (:maxRoomsCount IS NULL OR p.roomsCount <= :maxRoomsCount) " +
//            "AND (:minBathroomsCount IS NULL OR p.bathroomsCount >= :minBathroomsCount) " +
//            "AND (:maxBathroomsCount IS NULL OR p.bathroomsCount <= :maxBathroomsCount) " +
//            "AND (COALESCE(:heating) IS NULL OR p.heating IN :heating) " +
//            "AND (COALESCE(:constructionTypes) IS NULL OR p.constructionType IN :constructionTypes) " +
//            "AND (:minConstructionYear IS NULL OR p.constructionYear >= :minConstructionYear) " +
//            "AND (:maxConstructionYear IS NULL OR p.constructionYear <= :maxConstructionYear) " +
//            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
//            "AND (COALESCE(:ownershipTypes) IS NULL OR p.ownershipType IN :ownershipTypes)"
//    )
//    Page<Property> searchProperties(@Param("propertyTypes") List<PropertyType> propertyTypes,
//                                  @Param("propertyTransferType") PropertyTransferType propertyTransferType,
//                                  @Param("minBuiltUpArea") Double minBuiltUpArea,
//                                  @Param("maxBuiltUpArea") Double maxBuiltUpArea,
//                                  @Param("minYardArea") Double minYardArea,
//                                  @Param("maxYardArea") Double maxYardArea,
//                                  @Param("minRoomsCount") Short minRoomsCount,
//                                  @Param("maxRoomsCount") Short maxRoomsCount,
//                                  @Param("minBathroomsCount") Short minBathroomsCount,
//                                  @Param("maxBathroomsCount") Short maxBathroomsCount,
//                                  @Param("heating") List<String> heating,
//                                  @Param("constructionTypes") List<String> constructionTypes,
//                                  @Param("minConstructionYear") Short minConstructionYear,
//                                  @Param("maxConstructionYear") Short maxConstructionYear,
//                                  @Param("minPrice") BigDecimal minPrice,
//                                  @Param("maxPrice") BigDecimal maxPrice,
//                                  @Param("ownershipTypes") List<String> ownershipTypes,
//                                  Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM Property p " +
            "WHERE (:a IS NULL OR p.propertyType IN :a) " +
            "AND (:b IS NULL OR p.propertyTransferType = :b) " +
            "AND (:c IS NULL OR p.buildUpArea >= :c) " +
            "AND (:d IS NULL OR p.buildUpArea <= :d) " +
            "AND (:e IS NULL OR p.yardArea >= :e) " +
            "AND (:f IS NULL OR p.yardArea <= :f) " /*+
            "AND (:g IS NULL OR p.roomsCount >= :g) " +
            "AND (:h IS NULL OR p.roomsCount <= :h) " +
            "AND (:i IS NULL OR p.bathroomsCount >= :i) " +
            "AND (:j IS NULL OR p.bathroomsCount <= :j) " +
            "AND (COALESCE(:k) IS NULL OR p.heating IN :k) " +
            "AND (COALESCE(:l) IS NULL OR p.constructionType IN :l) " +
            "AND (:m IS NULL OR p.constructionYear >= :m) " +
            "AND (:n IS NULL OR p.constructionYear <= :n) " +
            "AND (:o IS NULL OR p.price >= :o) " +
            "AND (:p IS NULL OR p.price <= :p) " +
            "AND (COALESCE(:q) IS NULL OR p.ownershipType IN :q)"*/
    )
    Page<Property> searchProperties(@Param("a") List<PropertyType> a,
                                    @Param("b") PropertyTransferType b,
                                    @Param("c") Double c,
                                    @Param("d") Double d,
                                    @Param("e") Double e,
                                    @Param("f") Double f,
                                    /*@Param("g") Short g,
                                    @Param("h") Short h,
                                    @Param("i") Short i,
                                    @Param("j") Short j,
                                    @Param("k") List<String> k,
                                    @Param("l") List<String> l,
                                    @Param("m") Short m,
                                    @Param("n") Short n,
                                    @Param("o") BigDecimal o,
                                    @Param("p") BigDecimal p,
                                    @Param("q") List<String> q,*/
                                    Pageable pageable);
}
