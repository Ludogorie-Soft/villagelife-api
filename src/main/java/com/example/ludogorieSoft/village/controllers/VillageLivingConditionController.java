package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import com.example.ludogorieSoft.village.services.VillageLivingConditionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageLivingConditions")
@AllArgsConstructor
public class VillageLivingConditionController {
    private final VillageLivingConditionService villageLivingConditionService;

    @GetMapping
    public ResponseEntity<List<VillageLivingConditionDTO>> getAllVillageLivingConditions() {
        return ResponseEntity.ok(villageLivingConditionService.getAllVillageLivingConditions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillageLivingConditionDTO> getVillageLivingConditionsById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villageLivingConditionService.getByID(id));
    }


    @PostMapping
    public ResponseEntity<VillageLivingConditionDTO> createVillageLivingConditions(@Valid @RequestBody VillageLivingConditionDTO villageLivingConditionsDTO) {
        VillageLivingConditionDTO createdVillageLivingCondition = villageLivingConditionService.createVillageLivingCondition(villageLivingConditionsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVillageLivingCondition);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VillageLivingConditionDTO> updateVillageLivingConditions(@PathVariable("id") Long id, @Valid @RequestBody VillageLivingConditionDTO villageLivingConditionDTO) {
        return ResponseEntity.ok(villageLivingConditionService.updateVillageLivingCondition(id, villageLivingConditionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VillageLivingConditions> deleteVillageLivingConditionsById(@PathVariable("id") Long id) {
        int rowsAffected = villageLivingConditionService.deleteVillageLivingConditions(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
