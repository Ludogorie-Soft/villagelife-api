package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
