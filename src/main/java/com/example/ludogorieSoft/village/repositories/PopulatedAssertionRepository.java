package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopulatedAssertionRepository extends JpaRepository<PopulatedAssertion,Long> {
    boolean existsByPopulatedAssertion(String populatedAssertion);
}
