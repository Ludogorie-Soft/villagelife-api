package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.services.VillageImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/villageImages")
@AllArgsConstructor
public class VillageImageController {
    private final VillageImageService villageImageService;
    @GetMapping("/village/{villageId}/images")
    public ResponseEntity<List<String>> getAllImagesForVillage(@PathVariable Long villageId, boolean status, String date) {

        List<String> base64Images = villageImageService.getAllImagesForVillage(villageId, status, date);
        if (!base64Images.isEmpty()) {
            return new ResponseEntity<>(base64Images, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/approved")
    public ResponseEntity<List<VillageDTO>> getAllApprovedVillageDTOsWithImages() {
        List<VillageDTO> villageDTOS = villageImageService.getAllApprovedVillageDTOsWithImages();
        if (!villageDTOS.isEmpty()) {
            return new ResponseEntity<>(villageDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
