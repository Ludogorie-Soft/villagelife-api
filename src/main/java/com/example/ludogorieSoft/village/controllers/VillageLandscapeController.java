package com.example.ludogorieSoft.village.controllers;


import com.example.ludogorieSoft.village.dtos.VillageLandscapeDTO;
import com.example.ludogorieSoft.village.model.VillageLandscape;
import com.example.ludogorieSoft.village.services.VillageLandscapeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageLandscapes")
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
    public ResponseEntity<VillageLandscapeDTO> createVillageLandscape(@RequestBody VillageLandscapeDTO villageLandscapeDTO, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/villageLandscape/{id}")
                .buildAndExpand(villageLandscapeService.createVillageLandscape(villageLandscapeDTO).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VillageLandscapeDTO> updateVillageLandscape(@PathVariable("id") Long id, @RequestBody VillageLandscapeDTO villageLandscapeDTO) {
        System.out.println("Controller!!!!!!!!!!!!!!!!!!!");
        return ResponseEntity.ok(villageLandscapeService.updateVillageLandscape(id, villageLandscapeDTO));
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
