package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillagePopulationAssertionDTO;
import com.example.ludogorieSoft.village.model.VillagePopulationAssertion;
import com.example.ludogorieSoft.village.services.VillagePopulationAssertionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villagePopulationAssertions")
@AllArgsConstructor
public class VillagePopulationAssertionController {
    private final VillagePopulationAssertionService villagePopulationAssertionService;

    @GetMapping
    public ResponseEntity<List<VillagePopulationAssertionDTO>> getAllVillagePopulationAssertions(){
        return ResponseEntity.ok(villagePopulationAssertionService.getAllVillagePopulationAssertion());
    }
    @GetMapping("/{id}")
    public ResponseEntity<VillagePopulationAssertionDTO> getVillagePopulationAssertionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villagePopulationAssertionService.getByID(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<VillagePopulationAssertionDTO> updateVillagePopulationAssertionById(@PathVariable("id") Long id, @RequestBody VillagePopulationAssertionDTO villagePopulationAssertionDTO) {
        return ResponseEntity.ok(villagePopulationAssertionService.updateVillagePopulationAssertion(id, villagePopulationAssertionDTO));
    }

    @PostMapping
    public ResponseEntity<VillagePopulationAssertionDTO> createVillagePopulationAssertion(@RequestBody VillagePopulationAssertionDTO villagePopulationAssertionDTO, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/villagePopulationAssertions/{id}")
                .buildAndExpand(villagePopulationAssertionService.createVillagePopulationAssertionDTO(villagePopulationAssertionDTO).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VillagePopulationAssertion> deleteVillagePopulationAssertionById(@PathVariable("id") Long id) {
        int rowsAffected = villagePopulationAssertionService.deleteVillagePopulationAssertion(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
