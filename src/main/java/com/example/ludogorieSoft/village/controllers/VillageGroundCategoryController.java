package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import com.example.ludogorieSoft.village.services.VillageGroundCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageGroundCategory")
@AllArgsConstructor
public class VillageGroundCategoryController {

    private final VillageGroundCategoryService villageGroundCategoryService;

    @GetMapping
    public ResponseEntity<List<VillageGroundCategoryDTO>> getAllVillageGroundCategories() {
        return ResponseEntity.ok(villageGroundCategoryService.getAllVillageGroundCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillageGroundCategoryDTO> getVillageGroundCategoryByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villageGroundCategoryService.getByID(id));
    }


    @PostMapping
    public ResponseEntity<VillageGroundCategoryDTO> createVillageGroundCategories(@Valid @RequestBody VillageGroundCategoryDTO villageGroundCategoryDTO) {
        VillageGroundCategoryDTO createdVillageGroundCategory = villageGroundCategoryService.createVillageGroundCategoryDTO(villageGroundCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVillageGroundCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VillageGroundCategoryDTO> updateVillageGroundCategory(@PathVariable("id") Long id, @Valid @RequestBody VillageGroundCategoryDTO villageGroundCategoryDTO) {
        return ResponseEntity.ok(villageGroundCategoryService.updateVillageGroundCategory(id, villageGroundCategoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VillageGroundCategory> deleteVillageGroundCategoryById(@PathVariable("id") Long id) {
        int rowsAffected = villageGroundCategoryService.deleteVillageGroundCategory(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
