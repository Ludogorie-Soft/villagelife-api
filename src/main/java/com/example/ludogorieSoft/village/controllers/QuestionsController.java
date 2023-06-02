package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.DTOs.QuestionsDTO;
import com.example.ludogorieSoft.village.Services.QuestionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@AllArgsConstructor
public class QuestionsController {

    private final QuestionsService questionsService;

    @GetMapping
    public ResponseEntity<List<QuestionsDTO>> getAllQuestions() {
        List<QuestionsDTO> questions = questionsService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionsDTO> getQuestionById(@PathVariable("id") Long id) {
        QuestionsDTO questionDTO = questionsService.getQuestionById(id);
        return ResponseEntity.ok(questionDTO);
    }

    @PostMapping
    public ResponseEntity<QuestionsDTO> createQuestion(@Valid @RequestBody QuestionsDTO questionDTO) {
        QuestionsDTO createdQuestion = questionsService.createQuestion(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionsDTO> updateQuestion(@PathVariable("id") Long id, @Valid @RequestBody QuestionsDTO questionDTO) {
        QuestionsDTO updatedQuestion = questionsService.updateQuestion(id, questionDTO);
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("id") Long id) {
        questionsService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
