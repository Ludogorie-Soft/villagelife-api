package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.services.PopulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<PopulationDTO> createPopulation(@Valid @RequestBody PopulationDTO populationDTO) {
        PopulationDTO createdPopulation = populationService.createPopulation(populationDTO);
        return new ResponseEntity<>(createdPopulation, HttpStatus.CREATED);
    }

    @PostMapping("/null")
    public ResponseEntity<Long> createPopulationWithNullValues() {
        Long populationID = populationService.createPopulationWithNullValues();
        return ResponseEntity.status(HttpStatus.OK).body(populationID);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopulationDTO> updatePopulation(@PathVariable("id") Long id, @Valid @RequestBody PopulationDTO populationDTO) {
        return ResponseEntity.ok(populationService.updatePopulation(id, populationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePopulationById(@PathVariable("id") Long id) {
        populationService.deletePopulationById(id);
        return new ResponseEntity<>("Population with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }


    @GetMapping("/{villageName}/{region}")
    public ResponseEntity<PopulationDTO> findPopulationByVillageNameAndRegion(@PathVariable("villageName") String villageName, @PathVariable("region") String region) {
        return ResponseEntity.ok(populationService.findPopulationDTOByVillageNameAndRegion(villageName, region));
    }

    @GetMapping("/village/{id}")
    public ResponseEntity<PopulationDTO> getPopulationByVillageId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(populationService.getPopulationByVillageId(id, true, null));
    }
}
