package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.services.VillageAnswerQuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @GetMapping("/village/{id}")
    public ResponseEntity<List<VillageAnswerQuestionDTO>> getVillageAnswerQuestionByVillageId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(id));
    }

    @PostMapping
    public ResponseEntity<VillageAnswerQuestionDTO> createVillageAnswerQuestion(@Valid @RequestBody VillageAnswerQuestionDTO villageAnswerQuestionDTO) {
        VillageAnswerQuestionDTO createdVillageAnswerQuestion = villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestionDTO);
        return new ResponseEntity<>(createdVillageAnswerQuestion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VillageAnswerQuestionDTO> updateVillageAnswerQuestion(@PathVariable("id") Long id, @Valid @RequestBody VillageAnswerQuestionDTO villageAnswerQuestionDTO) {
        return ResponseEntity.ok(villageAnswerQuestionService.updateVillageAnswerQuestion(id, villageAnswerQuestionDTO));
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
