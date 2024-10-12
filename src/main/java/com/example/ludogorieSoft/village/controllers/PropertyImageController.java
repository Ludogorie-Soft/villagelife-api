package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.services.PropertyImageService;
import com.example.ludogorieSoft.village.services.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/property-images")
@AllArgsConstructor
public class PropertyImageController {
    private final PropertyImageService propertyImageService;
//    @PostMapping
//    public ResponseEntity<List<PropertyImageDTO>> createPropertyImage(@Valid @RequestBody List<PropertyImageDTO> propertyImageDTOs) {
//        List<PropertyImageDTO> createdPropertyImages = propertyImageService.createPropertyImage(propertyImageDTOs);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdPropertyImages);
//    }
}
