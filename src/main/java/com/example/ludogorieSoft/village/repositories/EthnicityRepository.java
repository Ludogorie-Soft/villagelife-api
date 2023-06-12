package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.Ethnicity;
import com.example.ludogoriesoft.village.model.GroundCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EthnicityRepository extends JpaRepository<Ethnicity, Long> {
    boolean existsByEthnicityName(String ethnicityName);
    List<Ethnicity> findAllByOrderByIdAsc();
}
