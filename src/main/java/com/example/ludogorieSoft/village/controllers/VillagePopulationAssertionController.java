package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillagePopulationAssertionDTO;
import com.example.ludogorieSoft.village.model.VillagePopulationAssertion;
import com.example.ludogorieSoft.village.services.VillagePopulationAssertionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/village/{id}")
    public ResponseEntity<List<VillagePopulationAssertionDTO>> getVillagePopulationAssertionByVillageId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villagePopulationAssertionService.getVillagePopulationAssertionByVillageId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<VillagePopulationAssertionDTO> updateVillagePopulationAssertionById(@PathVariable("id") Long id, @Valid @RequestBody VillagePopulationAssertionDTO villagePopulationAssertionDTO) {
        return ResponseEntity.ok(villagePopulationAssertionService.updateVillagePopulationAssertion(id, villagePopulationAssertionDTO));
    }


    @PostMapping
    public ResponseEntity<VillagePopulationAssertionDTO> createVillagePopulationAssertion(@Valid @RequestBody VillagePopulationAssertionDTO villageLandscapeDTO) {
        VillagePopulationAssertionDTO createdVillagePopulationAssertion = villagePopulationAssertionService.createVillagePopulationAssertionDTO(villageLandscapeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVillagePopulationAssertion);
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
