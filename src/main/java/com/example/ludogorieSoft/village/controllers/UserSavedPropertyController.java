package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.UserSavedPropertyDTO;
import com.example.ludogorieSoft.village.services.UserSavedPropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-saved-properties")
@AllArgsConstructor
public class UserSavedPropertyController {
    private final UserSavedPropertyService userSavedPropertyService;

    @PostMapping
    public ResponseEntity<UserSavedPropertyDTO> createUserSavedProperty(@Valid @RequestBody UserSavedPropertyDTO UserSavedPropertyDTO) {
        UserSavedPropertyDTO userSavedPropertyDTO = userSavedPropertyService.createUserSavedProperty(UserSavedPropertyDTO);
        return new ResponseEntity<>(userSavedPropertyDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserSavedPropertyDTO>> getAllUserSavedProperties() {
        return ResponseEntity.ok(userSavedPropertyService.getAllUserSavedProperties());
    }

    @GetMapping("/property/{propertyId}/user/{userId}")
    public ResponseEntity<Boolean> isPropertySavedByPropertyIdAndAlternativeUserId(@PathVariable("propertyId") Long propertyId, @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userSavedPropertyService.isPropertySavedByPropertyIdAndAlternativeUserId(propertyId, userId));
    }

    @PostMapping("/toggle/property/{propertyId}/user/{userId}")
    public ResponseEntity<UserSavedPropertyDTO> togglePropertySavedByPropertyIdAndAlternativeUserId(@PathVariable("propertyId") Long propertyId, @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userSavedPropertyService.toggleUserSavedProperty(propertyId, userId));
    }
}
