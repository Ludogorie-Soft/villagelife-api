package com.example.ludogoriesoft.village.controllers;


import com.example.ludogoriesoft.village.services.VillageImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
@RestController
@RequestMapping("/api/v1/villageImages")
@AllArgsConstructor
public class VillageImageController {
    private final VillageImageService villageImageService;

    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        String imageDirectory = "src/main/resources/static/village_images/"; // Replace with the actual directory path where the images are stored
        String imagePath = imageDirectory + imageName;

        try {
            Resource imageResource = new UrlResource("file:" + imagePath);
            if (imageResource.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return ResponseEntity.ok().headers(headers).body(imageResource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
