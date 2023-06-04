package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.EthnicityDTO;
import com.example.ludogorieSoft.village.model.Ethnicity;
import com.example.ludogorieSoft.village.services_tests.EthnicityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> deleteEthnicityById(@PathVariable("id") Long id) {
        ethnicityService.deleteEthnicityById(id);
        return new ResponseEntity<>("Ethnicity with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
}
