package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.DTOs.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.Model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.Repositories.VillageAnswerQuestionRepository;
import com.example.ludogorieSoft.village.Services.VillageAnswerQuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageAnswerQuestion")
@AllArgsConstructor
public class VillageAnswerQuestionController {
    private final VillageAnswerQuestionService villageAnswerQuestionService;

    @GetMapping
    public ResponseEntity<List<VillageAnswerQuestionDTO>> getAllVillageAnswerQuestions() {
        return ResponseEntity.ok(villageAnswerQuestionService.getAllVillageAnswerQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillageAnswerQuestionDTO> getVillageAnswerQuestionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villageAnswerQuestionService.getVillageAnswerQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<VillageAnswerQuestionDTO> createVillageAnswerQuestion(@RequestBody VillageAnswerQuestion villageAnswerQuestion, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/villageAnswerQuestion/{id}")
                .buildAndExpand(villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestion).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VillageAnswerQuestionDTO> updateVillageAnswerQuestion(@PathVariable("id") Long id, @RequestBody VillageAnswerQuestion villageAnswerQuestion) {
        return ResponseEntity.ok(villageAnswerQuestionService.updateVillageAnswerQuestion(id, villageAnswerQuestion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VillageAnswerQuestion> deleteVillageAnswerQuestionById(@PathVariable("id") Long id) {
        int rowsAffected = villageAnswerQuestionService.deleteVillageAnswerQuestionById(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
