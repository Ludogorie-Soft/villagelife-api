package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VillageAnswerQuestionRepository extends JpaRepository<VillageAnswerQuestion, Long> {
    boolean existsByVillageIdAndQuestionIdAndAnswer(Long villageId, Long questionId, String answer);
    List<VillageAnswerQuestion> findByVillageId(Long villageId);
    List<VillageAnswerQuestion> findByVillageIdAndVillageStatus(Long villageId, boolean villageStatus);
    @Query("SELECT v FROM VillageAnswerQuestion v WHERE v.village.id = :villageId " +
            "AND v.villageStatus = :villageStatus " +
            "AND DATE_FORMAT(v.dateUpload, '%Y-%m-%d %H:%i:%s') = :localDateTime")
    List<VillageAnswerQuestion> findByVillageIdAndVillageStatusAndDateUpload(Long villageId, boolean villageStatus, String localDateTime);

    @Query("SELECT vaq.village.name AS villageName, vaq.answer AS answer " +
            "FROM VillageAnswerQuestion vaq " +
            "JOIN vaq.question q " +
            "WHERE q.questionName = :questionName")
    List<Object[]> findVillageNameAndAnswerByQuestionName(@Param("questionName") String questionName);
}
