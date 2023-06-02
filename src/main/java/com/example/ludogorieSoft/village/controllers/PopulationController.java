package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.services.PopulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/population")
@RequiredArgsConstructor
public class PopulationController {

    private final PopulationService populationService;

    @GetMapping
    public ResponseEntity<List<PopulationDTO>> getAllPopulation() {
        return ResponseEntity.ok(populationService.getAllPopulation());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PopulationDTO> getPopulationById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(populationService.getPopulationById(id));
    }
    @PostMapping
    public ResponseEntity<PopulationDTO> createPopulation(@RequestBody Population population, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/population/{id}")
                .buildAndExpand(populationService.createPopulation(population).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopulationDTO> updatePopulation(@PathVariable("id") Long id, @RequestBody Population population) {
        return ResponseEntity.ok(populationService.updatePopulation(id, population));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PopulationDTO> deletePopulationById(@PathVariable("id") Long id) {
        int rowsAffected = populationService.deletePopulationById(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
