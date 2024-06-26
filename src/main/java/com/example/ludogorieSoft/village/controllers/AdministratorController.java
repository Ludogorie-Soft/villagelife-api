package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdministratorController {
    private final AdministratorService administratorService;

    @GetMapping
    public ResponseEntity<List<AdministratorDTO>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorDTO> getAdministratorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(administratorService.getAdministratorById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdministratorDTO> updateAdministrator(@PathVariable("id") Long id, @Valid @RequestBody AdministratorRequest administratorRequest) {
        return ResponseEntity.ok(administratorService.updateAdministrator(id, administratorRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdministratorById(@PathVariable("id") Long id) {
        administratorService.deleteAdministratorById(id);
        return new ResponseEntity<>("Administrator with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }

}
