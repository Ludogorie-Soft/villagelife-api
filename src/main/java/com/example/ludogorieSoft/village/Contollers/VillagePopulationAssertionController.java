package com.example.ludogorieSoft.village.Contollers;

import com.example.ludogorieSoft.village.DTOs.VillagePopulationAssertionDTO;
import com.example.ludogorieSoft.village.Model.VillagePopulationAssertion;
import com.example.ludogorieSoft.village.Services.VillagePopulationAssertionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villagePopulationAssertions")
@RequiredArgsConstructor
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

    @PostMapping
    public ResponseEntity<VillagePopulationAssertionDTO> createVillagePopulationAssertion(@RequestBody VillagePopulationAssertion VillagePopulationAssertion, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/villagePopulationAssertions/{id}")
                .buildAndExpand(villagePopulationAssertionService.CreateVillagePopulationAssertionDTO(VillagePopulationAssertion).getId())
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
