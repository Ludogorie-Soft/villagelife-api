package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.services.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/properties")
@AllArgsConstructor
public class PropertyController {
    private PropertyService propertyService;
    @GetMapping("/{page}/{elements}")
    public ResponseEntity<Page<PropertyDTO>> getAllProperties(@PathVariable("page") int page, @PathVariable("elements") int elements) {
        return ResponseEntity.ok(propertyService.getAllPropertiesAndMainImage(page, elements));
    }
}
