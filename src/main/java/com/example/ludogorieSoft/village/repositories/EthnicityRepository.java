package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Ethnicity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EthnicityRepository extends JpaRepository<Ethnicity, Long> {
    boolean existsByEthnicityName(String ethnicityName);
    List<Ethnicity> findAllByOrderByIdAsc();
}
