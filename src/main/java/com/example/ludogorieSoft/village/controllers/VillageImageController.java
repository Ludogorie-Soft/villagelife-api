package com.example.ludogorieSoft.village.controllers;


import com.example.ludogorieSoft.village.services.VillageImageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/villageImages")
@AllArgsConstructor
public class VillageImageController {
    public final VillageImageService villageImageService;

}
