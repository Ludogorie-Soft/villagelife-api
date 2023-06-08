package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.PopulatedAssertion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopulatedAssertionRepository extends JpaRepository<PopulatedAssertion,Long> {
    boolean existsByPopulatedAssertionName(String populatedAssertionName);
}
