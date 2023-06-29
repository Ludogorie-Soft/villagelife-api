package com.example.ludogorieSoft.village.controllers;


import com.example.ludogorieSoft.village.services.VillageImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/villageImages")
@AllArgsConstructor
public class VillageImageController {
    public final VillageImageService villageImageService;
    @GetMapping("/getImageBytesFromMultipartFile")
    public ResponseEntity<List<byte[]>> getImageBytesFromMultipartFile(@RequestBody List<MultipartFile> images) {
        List<byte[]> imageBytes = villageImageService.getImageBytesFromMultipartFile(images);
        return ResponseEntity.ok(imageBytes);
    }
}
