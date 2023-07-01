package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.services.ObjectAroundVillageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/objectAroundVillage")
@AllArgsConstructor
public class ObjectAroundVillageController {

    private final ObjectAroundVillageService objectAroundVillageService;

    @GetMapping
    public ResponseEntity<List<ObjectAroundVillageDTO>> getAllObjectsAroundVillage() {
        return ResponseEntity.ok(objectAroundVillageService.getAllObjectsAroundVillage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectAroundVillageDTO> getObjectAroundVillageByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(objectAroundVillageService.getObjectAroundVillageById(id));
    }

    @PostMapping
    public ResponseEntity<ObjectAroundVillageDTO> createObjectAroundVillage(@Valid @RequestBody ObjectAroundVillageDTO objectAroundVillageDTO) {
        ObjectAroundVillageDTO createdObjectAroundVillage = objectAroundVillageService.createObjectAroundVillage(objectAroundVillageDTO);
        return new ResponseEntity<>(createdObjectAroundVillage, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjectAroundVillageDTO> updateObjectAroundVillage(@PathVariable("id") Long id, @Valid @RequestBody ObjectAroundVillageDTO objectAroundVillageDTO) {
        return ResponseEntity.ok(objectAroundVillageService.updateObjectAroundVillage(id, objectAroundVillageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObjectAroundVillageById(@PathVariable("id") Long id) {
        objectAroundVillageService.deleteObjectAroundVillageById(id);
        return new ResponseEntity<>("ObjectAroundVillage with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
}
