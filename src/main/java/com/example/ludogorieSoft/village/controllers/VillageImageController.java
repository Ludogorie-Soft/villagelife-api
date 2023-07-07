package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageResponse;
import com.example.ludogorieSoft.village.services.VillageImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/villageImages")
@AllArgsConstructor
public class VillageImageController {
    private final VillageImageService villageImageService;
    @GetMapping("/village/{villageId}/images")
    public ResponseEntity<List<String>> getAllImagesForVillage(@PathVariable Long villageId) {
        List<String> base64Images = villageImageService.getAllImagesForVillage(villageId);
        if (!base64Images.isEmpty()) {
            return new ResponseEntity<>(base64Images, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //@GetMapping("/all")
    //public ResponseEntity<List<VillageImageResponse>> getAllVillageImageResponses() {
    //    List<VillageImageResponse> villageImageResponses = villageImageService.getAllVillageImages();
    //    if (!villageImageResponses.isEmpty()) {
    //        return new ResponseEntity<>(villageImageResponses, HttpStatus.OK);
    //    } else {
    //        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //    }
    //}
    @GetMapping("/all")
    public ResponseEntity<List<VillageDTO>> getAllVillageDTOsWithImages() {
        List<VillageDTO> villageDTOS = villageImageService.getAllVillageDTOsWithImages();
        if (!villageDTOS.isEmpty()) {
            return new ResponseEntity<>(villageDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
