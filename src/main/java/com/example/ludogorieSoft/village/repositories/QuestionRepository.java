package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    boolean existsByQuestion(String question);
}
