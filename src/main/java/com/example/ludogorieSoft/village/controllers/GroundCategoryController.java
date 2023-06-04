package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.GroundCategoryDTO;
import com.example.ludogorieSoft.village.model.GroundCategory;
import com.example.ludogorieSoft.village.services_tests.GroundCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groundCategory")
@AllArgsConstructor
public class GroundCategoryController {
    private final GroundCategoryService groundCategoryService;

    @GetMapping
    public ResponseEntity<List<GroundCategoryDTO>> getAllGroundCategories() {
        return ResponseEntity.ok(groundCategoryService.getAllGroundCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroundCategoryDTO> getGroundCategoryByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(groundCategoryService.getByID(id));
    }

    @PostMapping
    public ResponseEntity<GroundCategoryDTO> createGroundCategory(@Valid @RequestBody GroundCategoryDTO groundCategoryDTO) {
        GroundCategoryDTO createdGroundCategoryDTO = groundCategoryService.createGroundCategoryDTO(groundCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroundCategoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroundCategoryDTO> updateGroundCategory(@PathVariable("id") Long id, @Valid @RequestBody GroundCategoryDTO groundCategoryDTO) {
        GroundCategoryDTO updatedGroundCategoryDTO = groundCategoryService.updateGroundCategory(id, groundCategoryDTO);
        return ResponseEntity.ok(updatedGroundCategoryDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroundCategoryById(@PathVariable("id") Long id) {
        groundCategoryService.deleteGroundCategory(id);
        return new ResponseEntity<>("Ground Category with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }

}
