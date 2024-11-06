package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.UserSearchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserSearchDataRepository extends JpaRepository<UserSearchData, Long> {

    UserSearchData findBySearchNameAndAlternativeUserId(@Param("searchName") String searchName, @Param("alternativeUserId") Long id);
}
