package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.services.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/v1/properties")
@AllArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;
    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@Valid @RequestBody PropertyDTO propertyDTO) {
        PropertyDTO createdProperty = propertyService.createProperty(propertyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
    }

    @GetMapping("/{page}/{elements}")
    public ResponseEntity<Page<PropertyDTO>> getAllProperties(@PathVariable("page") int page, @PathVariable("elements") int elements) {
        return ResponseEntity.ok(propertyService.getAllPropertiesAndMainImage(page, elements));
    }

    @GetMapping("/village/{villageId}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByVillageId(@PathVariable("villageId") Long villageId) {
        return ResponseEntity.ok(propertyService.getAllPropertiesByVillageIdAndMainImage(villageId));
    }
}
