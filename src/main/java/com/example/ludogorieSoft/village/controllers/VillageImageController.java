package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.services.VillageImageService;
import lombok.AllArgsConstructor;
import org.apache.tika.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageImages")
@AllArgsConstructor
public class VillageImageController {
    private final VillageImageService villageImageService;
    @GetMapping("/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        Resource imageResource = villageImageService.getImage(imageName);
        if (imageResource != null) {
            try {
                InputStream inputStream = imageResource.getInputStream();
                byte[] imageBytes = IOUtils.toByteArray(inputStream);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/village/{villageId}/images")
    public ResponseEntity<List<String>> getAllImagesForVillage(@PathVariable Long villageId) {
        List<String> base64Images = villageImageService.getAllImagesForVillage(villageId);
        if (!base64Images.isEmpty()) {
            return new ResponseEntity<>(base64Images, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
