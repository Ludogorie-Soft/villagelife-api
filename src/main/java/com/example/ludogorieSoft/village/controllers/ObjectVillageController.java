package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.services.ObjectVillageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<ObjectVillageDTO> updateObjectVillageByID(@PathVariable Long id, @Valid @RequestBody ObjectVillageDTO objectVillageDTO) {
        return ResponseEntity.ok(objectVillageService.updateObjectVillageById(id, objectVillageDTO));
    }

    @PostMapping
    public ResponseEntity<ObjectVillageDTO> createObjectVillage(@Valid @RequestBody ObjectVillageDTO objectVillageDTO) {
        ObjectVillageDTO createdObjectVillage = objectVillageService.createObjectVillage(objectVillageDTO);
        return new ResponseEntity<>(createdObjectVillage, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObjectVillageById(@PathVariable("id") Long id) {
        objectVillageService.deleteObjectVillageById(id);
        return new ResponseEntity<>("ObjectVillage with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }

}
