package com.example.ludogorieSoft.village.controllers;


import com.example.ludogorieSoft.village.DTOs.VillageLandscapeDTO;
import com.example.ludogorieSoft.village.Model.VillageLandscape;
import com.example.ludogorieSoft.village.Services.VillageLandscapeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageLandscape")
@AllArgsConstructor
public class VillageLandscapeController {
    private final VillageLandscapeService villageLandscapeService;

    @GetMapping
    public ResponseEntity<List<VillageLandscapeDTO>> getAllVillageLandscapes() {
        return ResponseEntity.ok(villageLandscapeService.getAllVillageLandscapes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillageLandscapeDTO> getVillageLandscapeById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villageLandscapeService.getVillageLandscapeById(id));
    }

    @PostMapping
    public ResponseEntity<VillageLandscapeDTO> createVillageLandscape(@RequestBody VillageLandscape villageLandscape, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/villageLandscape/{id}")
                .buildAndExpand(villageLandscapeService.createVillageLandscape(villageLandscape).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VillageLandscapeDTO> updateVillageLandscape(@PathVariable("id") Long id, @RequestBody VillageLandscape villageLandscape) {
        return ResponseEntity.ok(villageLandscapeService.updateVillageLandscape(id, villageLandscape));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VillageLandscape> deleteVillageLandscapeById(@PathVariable("id") Long id) {
        int rowsAffected = villageLandscapeService.deleteVillageLandscapeById(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
