package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.AlternativeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlternativeUserRepository extends JpaRepository<AlternativeUser, Long> {
    boolean existsByUsername(String username);
    AlternativeUser findByUsername(String username);
}
