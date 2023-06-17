package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.AdministratorDTO;
import com.example.ludogoriesoft.village.dtos.AdministratorRequest;
import com.example.ludogoriesoft.village.model.Administrator;
import com.example.ludogoriesoft.village.services.AdministratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@AllArgsConstructor
public class AdministratorController {
    private final AdministratorService administratorService;

    @GetMapping
    public ResponseEntity<List<AdministratorDTO>> getAllAdministrators(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorDTO> getAdministratorById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(administratorService.getAdministratorById(id));
    }


    @PostMapping
    public ResponseEntity<AdministratorDTO> createAdministrator(@Valid @RequestBody AdministratorRequest administratorRequest, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(administratorService.createAdministrator(administratorRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministratorDTO> updateAdministrator(@PathVariable("id") Long id, @Valid @RequestBody AdministratorRequest administratorRequest) {
        return ResponseEntity.ok(administratorService.updateAdministrator(id, administratorRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdministratorById(@PathVariable("id") Long id) {
        administratorService.deleteAdministratorById(id);
        return new ResponseEntity<>("Administrator with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
    @GetMapping("/{username}")
    public Administrator getAdministratorByUsername(@PathVariable("username") String username) {

        return administratorService.findAdminByUsername(username);
    }
}
