package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.services.PropertyImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property-images")
@AllArgsConstructor
public class PropertyImageController {
    private PropertyImageService propertyImageService;

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<PropertyImageDTO>> getAllPropertyImagesByPropertyId(@PathVariable("propertyId") Long propertyId) {
        return ResponseEntity.ok(propertyImageService.getPropertyImagesByPropertyId(propertyId));
    }

}
