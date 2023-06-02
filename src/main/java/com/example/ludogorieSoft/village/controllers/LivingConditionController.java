package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.LivingConditionsDTO;
import com.example.ludogorieSoft.village.model.LivingConditions;
import com.example.ludogorieSoft.village.services.LivingConditionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/livingConditions")
@AllArgsConstructor
public class LivingConditionController {
    private final LivingConditionService livingConditionService;

    @GetMapping
    public ResponseEntity<List<LivingConditionsDTO>> getAllLivingConditions() {
        return ResponseEntity.ok(livingConditionService.getAllLivingConditions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivingConditionsDTO> getLivingConditionsByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(livingConditionService.getLivingConditionById(id));
    }

    @PostMapping
    public ResponseEntity<LivingConditionsDTO> createLivingConditions(@Valid @RequestBody LivingConditionsDTO livingConditionDTO) {
        LivingConditionsDTO createdLivingCondition = livingConditionService.createLivingCondition(livingConditionDTO);
        return new ResponseEntity<>(createdLivingCondition, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivingConditionsDTO> updateLivingCondition(@Valid @PathVariable("id") Long id, @RequestBody LivingConditions livingCondition) {
        return ResponseEntity.ok(livingConditionService.updateLivingCondition(id, livingCondition));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLivingConditionById(@PathVariable("id") Long id) {
        livingConditionService.deleteLivingCondition(id);
        return new ResponseEntity<>("LivingCondition with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }

}
