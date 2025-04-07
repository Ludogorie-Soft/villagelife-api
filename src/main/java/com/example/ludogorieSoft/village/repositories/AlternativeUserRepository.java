package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.AlternativeUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlternativeUserRepository extends JpaRepository<AlternativeUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
    AlternativeUser findByUsername(String username);
    Optional<AlternativeUser> findByEmail(String email);
}
