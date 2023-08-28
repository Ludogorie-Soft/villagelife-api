package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
