package com.example.ludogorieSoft.village.controllers;



import com.example.ludogorieSoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogorieSoft.village.model.PopulatedAssertion;
import com.example.ludogorieSoft.village.services.PopulatedAssertionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/populated_assertions")
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
    public ResponseEntity<PopulatedAssertionDTO> createPopulatedAssertion(@RequestBody @Valid PopulatedAssertion populatedAssertion, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/population/{id}")
                .buildAndExpand(populatedAssertionService.createPopulatedAssertion(populatedAssertion).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopulatedAssertionDTO> updatePopulatedAssertionById(@PathVariable("id") Long id, @Valid @RequestBody PopulatedAssertion populatedAssertion) {
        return ResponseEntity.ok(populatedAssertionService.updatePopulatedAssertion(id, populatedAssertion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePopulatedAssertionById(@PathVariable("id") Long id) {
        populatedAssertionService.deletePopulatedAssertionById(id);
        return new ResponseEntity<>("PopulatedAssertion with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
}