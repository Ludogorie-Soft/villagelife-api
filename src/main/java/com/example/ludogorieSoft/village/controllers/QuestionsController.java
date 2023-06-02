package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.DTOs.QuestionsDTO;
import com.example.ludogorieSoft.village.Model.Questions;
import com.example.ludogorieSoft.village.Services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionsController {

    private final QuestionService questionService;
    @GetMapping
    public ResponseEntity<List<QuestionsDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionsDTO> getQuestionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionsDTO> createQuestion(@Valid @RequestBody Questions question, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/questions/{id}")
                .buildAndExpand(questionService.createQuestion(question).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionsDTO> updateQuestion(@PathVariable("id") Long id, @Valid @RequestBody Questions question) {
        return ResponseEntity.ok(questionService.updateQuestion(id, question));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QuestionsDTO> deleteQuestionById(@PathVariable("id") Long id) {
        int rowsAffected = questionService.deleteQuestionById(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}