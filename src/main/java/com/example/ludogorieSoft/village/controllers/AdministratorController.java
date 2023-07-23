package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.services.AdministratorService;
import com.example.ludogorieSoft.village.services.VillageService;
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
    private final VillageService villageService;

    @GetMapping
    public ResponseEntity<List<AdministratorDTO>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorDTO> getAdministratorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(administratorService.getAdministratorById(id));
    }


    @PostMapping
    public ResponseEntity<AdministratorDTO> createAdministrator(@Valid @RequestBody AdministratorRequest administratorRequest) {
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

    @GetMapping("village")
    public List<VillageResponse> getAllVillages(){
        return administratorService.getAllVillagesWithPopulation();
    }

    @DeleteMapping("/village-delete/{villageId}")
    public ResponseEntity<String> deleteVillageById(@PathVariable("villageId") Long villageId) {
        villageService.deleteVillage(villageId);
        return new ResponseEntity<>("Village with id: " + villageId + " has been deleted successfully!!", HttpStatus.OK);
    }
    @PostMapping("/approve/{id}")
    public ResponseEntity<String> changeVillageStatus(@PathVariable(name = "id") Long id){
        VillageDTO villageDTO = villageService.getVillageById(id);
        villageService.updateVillage(id,villageDTO);
        return new ResponseEntity<>("Status of village with ID: " + id + " changed successfully!!!", HttpStatus.OK);
    }

}
