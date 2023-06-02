package com.example.ludogorieSoft.village.Repositories;

import com.example.ludogorieSoft.village.Model.GroundCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroundCategoryRepository extends JpaRepository<GroundCategory, Long> {

    boolean existsByGroundCategory(String groundCategory);


}
