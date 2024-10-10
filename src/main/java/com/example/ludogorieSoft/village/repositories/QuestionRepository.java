package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    boolean existsByQuestionName(String question);
    List<Question> findAllByOrderByIdAsc();
}
