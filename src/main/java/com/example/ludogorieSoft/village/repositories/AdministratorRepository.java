package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    boolean existsByUsername(String username);
    Administrator findByUsername(String username);
}
