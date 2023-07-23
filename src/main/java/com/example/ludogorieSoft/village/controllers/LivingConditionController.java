package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.dtos.response.LivingConditionResponse;
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
    public ResponseEntity<List<LivingConditionDTO>> getAllLivingConditions() {
        return ResponseEntity.ok(livingConditionService.getAllLivingConditions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivingConditionDTO> getLivingConditionsByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(livingConditionService.getLivingConditionById(id));
    }

    @PostMapping
    public ResponseEntity<LivingConditionDTO> createLivingConditions(@Valid @RequestBody LivingConditionDTO livingConditionDTO) {
        LivingConditionDTO createdLivingCondition = livingConditionService.createLivingCondition(livingConditionDTO);
        return new ResponseEntity<>(createdLivingCondition, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivingConditionDTO> updateLivingCondition(@PathVariable("id") Long id, @Valid @RequestBody LivingConditionDTO livingConditionDTO) {
        return ResponseEntity.ok(livingConditionService.updateLivingCondition(id, livingConditionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLivingConditionById(@PathVariable("id") Long id) {
        livingConditionService.deleteLivingCondition(id);
        return new ResponseEntity<>("LivingCondition with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }

    @GetMapping("/conditions/village/{villageId}")
    public ResponseEntity<List<LivingConditionResponse>> getLivingConditionResponseByVillageId(@PathVariable("villageId") Long villageId) {
        return ResponseEntity.ok(livingConditionService.getLivingConditionResponses(villageId));
    }

}
