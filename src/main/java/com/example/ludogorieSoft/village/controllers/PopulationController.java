package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.services.PopulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/populations")
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
    public ResponseEntity<PopulationDTO> createPopulation(@RequestBody @Valid PopulationDTO populationDTO, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/population/{id}")
                .buildAndExpand(populationService.createPopulation(populationDTO).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopulationDTO> updatePopulation(@PathVariable("id") Long id,@Valid @RequestBody PopulationDTO populationDTO) {
        return ResponseEntity.ok(populationService.updatePopulation(id, populationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePopulationById(@PathVariable("id") Long id) {
        populationService.deletePopulationById(id);
        return new ResponseEntity<>("Population with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
}
