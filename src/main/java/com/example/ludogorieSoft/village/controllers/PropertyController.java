package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.services.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

}
