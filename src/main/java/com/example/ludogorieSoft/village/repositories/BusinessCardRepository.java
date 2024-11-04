package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.BusinessCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {
    boolean existsByEmail(String email);
}
