package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogoriesoft.village.services.PopulatedAssertionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/populatedAssertions")
@RequiredArgsConstructor
public class PopulatedAssertionController {

    private final PopulatedAssertionService populatedAssertionService;

    @GetMapping
    public ResponseEntity<List<PopulatedAssertionDTO>> getAllPopulatedAssertion() {
        return ResponseEntity.ok(populatedAssertionService.getAllPopulatedAssertion());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PopulatedAssertionDTO> getPopulatedAssertionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(populatedAssertionService.getPopulatedAssertionById(id));
    }



    @PostMapping
    public ResponseEntity<PopulatedAssertionDTO> createPopulatedAssertion(@Valid @RequestBody PopulatedAssertionDTO populatedAssertionDTO) {
        PopulatedAssertionDTO createdPopulation = populatedAssertionService.createPopulatedAssertion(populatedAssertionDTO);
        return new ResponseEntity<>(createdPopulation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopulatedAssertionDTO> updatePopulatedAssertionById(@PathVariable("id") Long id, @Valid @RequestBody PopulatedAssertionDTO populatedAssertionDTO) {
        return ResponseEntity.ok(populatedAssertionService.updatePopulatedAssertion(id, new PopulatedAssertionDTO()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePopulatedAssertionById(@PathVariable("id") Long id) {
        populatedAssertionService.deletePopulatedAssertionById(id);
        return new ResponseEntity<>("PopulatedAssertion with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
}