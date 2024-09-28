package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyUser;
import com.example.ludogorieSoft.village.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PropertyRepository extends JpaRepository<Property, Long> {
}
