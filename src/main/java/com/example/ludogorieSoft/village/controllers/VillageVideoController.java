package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.services.VillageVideoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/villageVideos")
@AllArgsConstructor
public class VillageVideoController {
    private final VillageVideoService villageVideoService;
}
