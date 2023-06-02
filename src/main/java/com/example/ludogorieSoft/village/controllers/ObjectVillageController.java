package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.services.ObjectVillageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/objectVillages")
@AllArgsConstructor
public class ObjectVillageController {
    private final ObjectVillageService objectVillageService;

    @GetMapping
    public ResponseEntity<List<ObjectVillageDTO>> getAllObjectVillages() {
        return ResponseEntity.ok(objectVillageService.getAllObjectVillages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectVillageDTO> getObjectVillageByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(objectVillageService.getObjectVillageById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjectVillageDTO> updateObjectVillageByID(@PathVariable Long id, @RequestBody ObjectVillageDTO objectVillageDTO) {
        return ResponseEntity.ok(objectVillageService.updateObjectVillageById(id, objectVillageDTO));
    }

    @PostMapping
    public ResponseEntity<ObjectVillageDTO> createObjectVillage(@RequestBody ObjectVillageDTO objectVillageDTO, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/objectVillages/{id}")
                .buildAndExpand(objectVillageService.createObjectVillage(objectVillageDTO).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObjectVillageById(@PathVariable("id") Long id) {
        objectVillageService.deleteObjectVillageById(id);
        return new ResponseEntity<>("ObjectVillage with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }

}
