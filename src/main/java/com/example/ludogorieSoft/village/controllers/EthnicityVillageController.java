package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.EthnicityVillageDTO;
import com.example.ludogorieSoft.village.model.EthnicityVillage;
import com.example.ludogorieSoft.village.services_tests.EthnicityVillageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageEthnicities")
@AllArgsConstructor
public class EthnicityVillageController {
    private final EthnicityVillageService ethnicityVillageService;

    @GetMapping
    public ResponseEntity<List<EthnicityVillageDTO>> getAllEthnicityVillages() {
        return ResponseEntity.ok(ethnicityVillageService.getAllEthnicityVillages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EthnicityVillageDTO> getEthnicityVillageByProductId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ethnicityVillageService.getEthnicityVillageById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EthnicityVillageDTO> updateEthnicityVillageById(@PathVariable Long id, @RequestBody EthnicityVillageDTO ethnicityVillageDTO) {
        return ResponseEntity.ok(ethnicityVillageService.updateEthnicityVillageById(id, ethnicityVillageDTO));
    }

    @PostMapping
    public ResponseEntity<EthnicityVillageDTO> createEthnicityVillage(@RequestBody EthnicityVillageDTO ethnicityVillageDTO, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/ethnicityvillages/{id}")
                .buildAndExpand(ethnicityVillageService.createEthnicityVillage(ethnicityVillageDTO).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<EthnicityVillage> deleteEthnicityVillageById(@PathVariable("id") Long id) {
        int rowsAffected = ethnicityVillageService.deleteEthnicityVillageById(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
