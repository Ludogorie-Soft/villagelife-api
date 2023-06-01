package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.DTOs.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.Model.VillageGroundCategory;
import com.example.ludogorieSoft.village.Services.VillageGroundCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageGroundCategory")
@AllArgsConstructor
public class VillageGroundCategoryController {

    private final VillageGroundCategoryService villageGroundCategoryService;

    @GetMapping
    public ResponseEntity<List<VillageGroundCategoryDTO>> getAllVillageGroundCategories() {
        System.out.println("TEST Controller :" + villageGroundCategoryService.getAllVillageGroundCategories());
        return ResponseEntity.ok(villageGroundCategoryService.getAllVillageGroundCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillageGroundCategoryDTO> getVillageGroundCategoryByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villageGroundCategoryService.getByID(id));
    }

    @PostMapping
    public ResponseEntity<VillageGroundCategoryDTO> createVillageGroundCategories(@RequestBody VillageGroundCategory VillageGroundCategory, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/villageGroundCategory/{id}")
                .buildAndExpand(villageGroundCategoryService.createVillageGroundCategoryDTO(VillageGroundCategory).getId())
                .toUri();
        return ResponseEntity.created(location).build();
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
