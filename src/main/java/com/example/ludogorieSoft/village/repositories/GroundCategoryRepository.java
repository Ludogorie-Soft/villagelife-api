package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.GroundCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroundCategoryRepository extends JpaRepository<GroundCategory, Long> {

    boolean existsByGroundCategoryName(String groundCategoryName);
    List<GroundCategory> findAllByOrderByIdAsc();
    GroundCategory findByGroundCategoryName(String groundCategoryName);
}
