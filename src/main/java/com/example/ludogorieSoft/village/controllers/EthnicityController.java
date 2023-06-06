package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.EthnicityDTO;
import com.example.ludogorieSoft.village.services.EthnicityService;
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
    public ResponseEntity<EthnicityDTO> createEthnicity(@RequestBody EthnicityDTO ethnicityDTO, UriComponentsBuilder uriComponentsBuilder) {
        EthnicityDTO createdEthnicityDTO = ethnicityService.createEthnicity(ethnicityDTO);
        URI location = uriComponentsBuilder.path("/api/v1/ethnicities/{id}")
                .buildAndExpand(createdEthnicityDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdEthnicityDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EthnicityDTO> updateEthnicity(@PathVariable("id") Long id, @RequestBody EthnicityDTO ethnicityDTO) {
        EthnicityDTO updatedEthnicityDTO = ethnicityService.updateEthnicity(id, ethnicityDTO);
        return ResponseEntity.ok(updatedEthnicityDTO);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEthnicityById(@PathVariable("id") Long id) {
        ethnicityService.deleteEthnicityById(id);
        return new ResponseEntity<>("Ethnicity with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
}
