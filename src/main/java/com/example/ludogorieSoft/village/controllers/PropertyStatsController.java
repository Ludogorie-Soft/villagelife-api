package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.services.PropertyStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/property-stats")
@AllArgsConstructor
public class PropertyStatsController {
    private final PropertyStatsService propertyStatsService;

    /*@PostMapping
    public ResponseEntity<PropertyStatsDTO> createPropertyStats(@RequestBody PropertyStatsDTO propertyStatsDTO) {
        PropertyStatsDTO createdPropertyStats = propertyStatsService.createPropertyStats(propertyStatsDTO);
        return new ResponseEntity<>(createdPropertyStats, HttpStatus.CREATED);
    }*/

    @PutMapping("/{propertyId}/increment-views")
    public ResponseEntity<PropertyStatsDTO> incrementPropertyViews(@PathVariable Long propertyId) {
        PropertyStatsDTO propertyStatsDTO = propertyStatsService.incrementViewsByPropertyId(propertyId);
        return new ResponseEntity<>(propertyStatsDTO, HttpStatus.OK);
    }
}
