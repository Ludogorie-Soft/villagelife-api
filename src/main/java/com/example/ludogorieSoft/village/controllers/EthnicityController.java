package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.DTOs.EthnicityDTO;
import com.example.ludogorieSoft.village.Model.Ethnicity;
import com.example.ludogorieSoft.village.Services.EthnicityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ethnicities")
@AllArgsConstructor
public class EthnicityController {
    private final EthnicityService ethnicityService;
    @GetMapping
    public ResponseEntity<List<EthnicityDTO>> getAllEthnicities() {
        return ResponseEntity.ok(ethnicityService.getAllEthnicities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EthnicityDTO> getEthnicityById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ethnicityService.getEthnicityById(id));
    }

    @PostMapping
    public ResponseEntity<EthnicityDTO> createEthnicity(@RequestBody Ethnicity ethnicity, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/ethnicities/{id}")
                .buildAndExpand(ethnicityService.createEthnicity(ethnicity).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<EthnicityDTO> updateEthnicity(@PathVariable("id") Long id, @RequestBody Ethnicity ethnicity) {
        return ResponseEntity.ok(ethnicityService.updateEthnicity(id, ethnicity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ethnicity> deleteEthnicityById(@PathVariable("id") Long id) {
        int rowsAffected = ethnicityService.deleteEthnicityById(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
