package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.DTOs.LandscapeDTO;
import com.example.ludogorieSoft.village.Model.Landscape;
import com.example.ludogorieSoft.village.Services.LandscapeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/landscapes")
@AllArgsConstructor
public class LandscapeController {
    private final LandscapeService landscapeService;
    @GetMapping
    public ResponseEntity<List<LandscapeDTO>> getAllLandscapes() {
        return ResponseEntity.ok(landscapeService.getAllLandscapes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LandscapeDTO> getLandscapeByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(landscapeService.getLandscapeById(id));
    }

    @PostMapping
    public ResponseEntity<LandscapeDTO> createLandscape(@Valid @RequestBody LandscapeDTO landscapeDTO) {
        LandscapeDTO createdLandscape = landscapeService.createLandscape(landscapeDTO);
        return new ResponseEntity<>(createdLandscape, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<LandscapeDTO> updateLandscape(@Valid @PathVariable("id") Long id, @RequestBody Landscape landscape) {
        return ResponseEntity.ok(landscapeService.updateLandscape(id, landscape));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLandscapeById(@PathVariable("id") Long id) {
        landscapeService.deleteLandscape(id);
        return new ResponseEntity<>("Landscape with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }


}
