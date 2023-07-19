package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VillageAnswerQuestionRepository extends JpaRepository<VillageAnswerQuestion, Long> {
    boolean existsByVillageIdAndQuestionIdAndAnswer(Long villageId, Long questionId, String answer);
}
