package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VillageAnswerQuestionRepository extends JpaRepository<VillageAnswerQuestion, Long> {
    boolean existsByVillageIdAndQuestionIdAndAnswer(Long villageId, Long questionId, String answer);
    List<VillageAnswerQuestion> findByVillageId(Long villageId);
    List<VillageAnswerQuestion> findByVillageIdAndVillageStatus(Long villageId, boolean villageStatus);
    @Query("SELECT v FROM VillageAnswerQuestion v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND (v.dateDeleted IS NULL) " +
            "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<VillageAnswerQuestion> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, boolean villageStatus, String localDateTime);

}
