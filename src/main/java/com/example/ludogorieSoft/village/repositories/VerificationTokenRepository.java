package com.example.ludogorieSoft.village.repositories;


import com.example.ludogorieSoft.village.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
