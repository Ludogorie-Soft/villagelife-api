package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageVideoDTO;
import com.example.ludogorieSoft.village.services.VillageVideoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/villageVideos")
@AllArgsConstructor
public class VillageVideoController {
    private final VillageVideoService villageVideoService;
    @GetMapping("/village/{villageId}")
    public ResponseEntity<List<VillageVideoDTO>> getAllVideosByVillageId(@PathVariable("villageId") Long villageId) {
        return ResponseEntity.ok(villageVideoService.getAllVideosByVillageId(villageId));
    }
    @GetMapping("/approved/village/{villageId}")
    public ResponseEntity<List<VillageVideoDTO>> getAllApprovedVideosByVillageId(@PathVariable("villageId") Long villageId) {
        return ResponseEntity.ok(villageVideoService.getAllApprovedVideosByVillageId(villageId));
    }
}
