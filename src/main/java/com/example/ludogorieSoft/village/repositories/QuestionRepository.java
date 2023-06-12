package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.Ethnicity;
import com.example.ludogoriesoft.village.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    boolean existsByQuestionName(String question);
    List<Question> findAllByOrderByIdAsc();
}
