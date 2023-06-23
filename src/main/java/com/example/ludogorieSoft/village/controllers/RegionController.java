package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.RegionDTO;
import com.example.ludogoriesoft.village.services.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(regionService.getRegionById(id));
    }

    @PostMapping
    public ResponseEntity<RegionDTO> createRegion(@Valid @RequestBody RegionDTO regionDTO, UriComponentsBuilder uriComponentsBuilder) {
        RegionDTO createdRegionDTO = regionService.createRegion(regionDTO);
        URI location = uriComponentsBuilder.path("/api/v1/regions/{id}")
                .buildAndExpand(createdRegionDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdRegionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateRegion(@PathVariable("id") Long id, @Valid @RequestBody RegionDTO regionDTO) {
        RegionDTO updatedRegionDTO = regionService.updateRegion(id, regionDTO);
        return ResponseEntity.ok(updatedRegionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegionById(@PathVariable("id") Long id) {
        regionService.deleteRegionById(id);
        return new ResponseEntity<>("Region with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }

}
