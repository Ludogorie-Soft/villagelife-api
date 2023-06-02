package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionsDTO;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import com.example.ludogorieSoft.village.services.VillageLivingConditionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageLivingConditions")
@AllArgsConstructor
public class VillageLivingConditionsController {
    private final VillageLivingConditionsService villageLivingConditionsService;

    @GetMapping
    public ResponseEntity<List<VillageLivingConditionsDTO>> getAllVillageLivingConditions() {

        return ResponseEntity.ok(villageLivingConditionsService.getAllVillageLivingConditions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillageLivingConditionsDTO> getVillageLivingConditionsById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villageLivingConditionsService.getByID(id));
    }

    @PostMapping
    public ResponseEntity<VillageLivingConditionsDTO> createVillageLivingConditions(@RequestBody VillageLivingConditions villageLivingConditions, UriComponentsBuilder uriComponentsBuilder) {
        System.out.println(villageLivingConditions);
        URI location = uriComponentsBuilder.path("/api/v1/villageLivingConditions/{id}")
                .buildAndExpand(villageLivingConditionsService.createVillageLivingConditionsDTO(villageLivingConditions).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<VillageLivingConditions> deleteVillageLivingConditionsById(@PathVariable("id") Long id) {
        int rowsAffected = villageLivingConditionsService.deleteVillageLivingConditions(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
