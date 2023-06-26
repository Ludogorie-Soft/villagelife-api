package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.AdministratorDTO;
import com.example.ludogoriesoft.village.dtos.AdministratorRequest;
import com.example.ludogoriesoft.village.dtos.response.VillageResponse;
import com.example.ludogoriesoft.village.model.Administrator;
import com.example.ludogoriesoft.village.services.AdministratorService;
import com.example.ludogoriesoft.village.services.VillageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@AllArgsConstructor
public class AdministratorController {
    private final AdministratorService administratorService;
    private final VillageService villageService;

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
    @PutMapping("/update/{id}")
    public ResponseEntity<AdministratorDTO> updateAdministrator(@PathVariable("id") Long id, @Valid @RequestBody AdministratorRequest administratorRequest) {
        return ResponseEntity.ok(administratorService.updateAdministrator(id, administratorRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdministratorById(@PathVariable("id") Long id) {
        administratorService.deleteAdministratorById(id);
        return new ResponseEntity<>("Administrator with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }
    @GetMapping("/username/{username}")
    public Administrator getAdministratorByUsername(@PathVariable("username") String username,@RequestHeader("Authorization") String token) {
        return administratorService.findAdminByUsername(username);
    }
    @GetMapping("village")
    public List<VillageResponse> getAllVillages(@RequestHeader("Authorization") String token){
       return administratorService.getAllVillagesWithPopulation();
    }
}
