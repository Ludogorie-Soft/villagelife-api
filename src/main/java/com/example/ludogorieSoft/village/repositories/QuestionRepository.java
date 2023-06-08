package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    boolean existsByQuestionName(String question);
}
