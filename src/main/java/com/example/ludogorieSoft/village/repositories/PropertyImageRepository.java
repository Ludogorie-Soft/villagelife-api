package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    List<PropertyImage> findByProperty_VillageIdAndDeletedAtIsNull(Long villageId);
}
