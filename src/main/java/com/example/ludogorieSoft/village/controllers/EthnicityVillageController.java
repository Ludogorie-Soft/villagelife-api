package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.EthnicityVillageDTO;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import com.example.ludogoriesoft.village.services.EthnicityVillageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
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
    public ResponseEntity<EthnicityVillageDTO> getEthnicityVillageById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ethnicityVillageService.getEthnicityVillageById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EthnicityVillageDTO> updateEthnicityVillageById(@PathVariable Long id, @Valid  @RequestBody EthnicityVillageDTO ethnicityVillageDTO) {
        return ResponseEntity.ok(ethnicityVillageService.updateEthnicityVillageById(id, ethnicityVillageDTO));
    }

    @PostMapping
    public ResponseEntity<EthnicityVillageDTO> createEthnicityVillage(@Valid @RequestBody EthnicityVillageDTO ethnicityVillageDTO, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/villageEthnicities/{id}")
                .buildAndExpand(ethnicityVillageService.createEthnicityVillage(ethnicityVillageDTO).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEthnicityVillageById(@PathVariable("id") Long id) {
        try {
            ethnicityVillageService.deleteEthnicityVillageById(id);
            return new ResponseEntity<>("Ethnicity in Village with id: " + id + " has been deleted successfully!!", HttpStatus.OK);

        } catch (ApiRequestException e) {
            String errorMessage = "Ethnicity in Village with id " + id + " not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

}
