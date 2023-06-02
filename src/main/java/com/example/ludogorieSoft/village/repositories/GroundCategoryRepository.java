package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.GroundCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroundCategoryRepository extends JpaRepository<GroundCategory, Long> {

    boolean existsByGroundCategory(String groundCategory);


}
