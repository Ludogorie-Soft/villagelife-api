package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.AdministratorRequest;
import com.example.ludogorieSoft.village.services_tests.AdministratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@AllArgsConstructor
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

    @PostMapping
    public ResponseEntity<AdministratorDTO> createAdministrator(@RequestBody AdministratorRequest administratorRequest, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/admins/{id}")
                .buildAndExpand(administratorService.createAdministrator(administratorRequest).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministratorDTO> updateAdministrator(@PathVariable("id") Long id, @RequestBody AdministratorRequest administratorRequest) {
        return ResponseEntity.ok(administratorService.updateAdministrator(id, administratorRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdministratorById(@PathVariable("id") Long id) {
        administratorService.deleteAdministratorById(id);
        return new ResponseEntity<>("Administrator with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
}
