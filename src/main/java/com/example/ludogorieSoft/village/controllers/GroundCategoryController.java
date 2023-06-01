package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.DTOs.GroundCategoryDTO;
import com.example.ludogorieSoft.village.Model.GroundCategory;
import com.example.ludogorieSoft.village.Services.GroundCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groundCategory")
@AllArgsConstructor
public class GroundCategoryController {
    private final GroundCategoryService groundCategoryService;

    @GetMapping
    public ResponseEntity<List<GroundCategoryDTO>> getAllGroundCategories() {
        System.out.println("TEST Controller :" + groundCategoryService.getAllGroundCategories());
        return ResponseEntity.ok(groundCategoryService.getAllGroundCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroundCategoryDTO> getGroundCategoryByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(groundCategoryService.getByID(id));
    }

    @PostMapping
    public ResponseEntity<GroundCategoryDTO> createGroundCategories(@RequestBody GroundCategory GroundCategory, UriComponentsBuilder uriComponentsBuilder) {
        URI location = uriComponentsBuilder.path("/api/v1/groundCategory/{id}")
                .buildAndExpand(groundCategoryService.createGroundCategoryDTO(GroundCategory).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroundCategory> deleteGroundCategoryById(@PathVariable("id") Long id) {
        int rowsAffected = groundCategoryService.deleteGroundCategory(id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
