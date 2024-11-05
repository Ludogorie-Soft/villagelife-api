package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.UserSavedProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSavedPropertyRepository extends JpaRepository<UserSavedProperty,Long> {
    @Query("SELECT CASE WHEN COUNT(usp) > 0 THEN true ELSE false END " +
            "FROM UserSavedProperty usp " +
            "WHERE usp.property.id = :propertyId " +
            "AND usp.user.id = :userId " +
            "AND usp.deletedAt IS NULL")
    Boolean existsByPropertyIdAndUserId(@Param("propertyId") Long propertyId, @Param("userId") Long userId);
    UserSavedProperty findByPropertyIdAndUserId(@Param("propertyId") Long propertyId, @Param("userId") Long userId);
}
