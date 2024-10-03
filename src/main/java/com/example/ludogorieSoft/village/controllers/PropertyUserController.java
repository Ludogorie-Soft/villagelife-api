package com.example.ludogorieSoft.village.controllers;


import com.example.ludogorieSoft.village.dtos.PropertyUserDTO;

import com.example.ludogorieSoft.village.services.PropertyUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@RestController
@RequestMapping("api/v1/property-users")
@AllArgsConstructor
public class PropertyUserController {
    private final PropertyUserService propertyUserService;
    @PostMapping
    public ResponseEntity<PropertyUserDTO> createPropertyUser(@Valid @RequestBody PropertyUserDTO propertyUserDTO) {
        PropertyUserDTO createdPropertyUser = propertyUserService.createPropertyUser(propertyUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPropertyUser);
    }
}
