package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.UserSearchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserSearchDataRepository extends JpaRepository<UserSearchData, Long> {

    UserSearchData findBySearchNameAndAlternativeUserIdAndDeletedAtIsNull(@Param("searchName") String searchName, @Param("alternativeUserId") Long id);

    List<UserSearchData> findAllByAlternativeUserIdAndDeletedAtIsNull(@Param("alternativeUserId") Long id);

    Optional<UserSearchData> findByIdAndDeletedAtIsNull(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE UserSearchData e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
    void softDeleteById(Long id);
}
