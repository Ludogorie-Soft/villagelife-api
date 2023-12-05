package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.services.VillageImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villageImages")
@AllArgsConstructor
public class VillageImageController {
    private final VillageImageService villageImageService;

    @GetMapping("/village/{villageId}/images")
    public ResponseEntity<List<String>> getAllImagesForVillageByStatusAndDate(@PathVariable Long villageId, boolean status, String date) {

        List<String> base64Images = villageImageService.getAllImagesForVillageByStatusAndDate(villageId, status, date);
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
    @GetMapping("/approved/{page}/{elements}")
    public ResponseEntity<List<VillageDTO>> getAllApprovedVillageDTOsWithImages(@PathVariable("page") int page, @PathVariable("elements") int elements) {
        List<VillageDTO> villageDTOS = villageImageService.getApprovedVillageDTOsWithImages(page, elements);
        if (!villageDTOS.isEmpty()) {
            return new ResponseEntity<>(villageDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/admin-upload")
    public ResponseEntity<List<byte[]>> adminUploadImages(@RequestParam("villageId") Long villageId,
                                                          @RequestBody List<byte[]> imageBytesList) {
        villageImageService.createImagePaths(imageBytesList, villageId, LocalDateTime.now(), true,null);
        return ResponseEntity.ok(imageBytesList);
    }

    @GetMapping("/with-base64/village/{villageId}")
    public ResponseEntity<List<VillageImageDTO>> getNotDeletedVillageImageDTOsByVillageId(@PathVariable("villageId") Long villageId) {
        return ResponseEntity.ok(villageImageService.getNotDeletedVillageImageDTOsByVillageId(villageId));
    }

    @GetMapping("/deleted/with-base64/village/{villageId}")
    public ResponseEntity<List<VillageImageDTO>> getDeletedVillageImageDTOsByVillageId(@PathVariable("villageId") Long villageId) {
        return ResponseEntity.ok(villageImageService.getDeletedVillageImageDTOsByVillageId(villageId));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<VillageImageDTO> rejectVillageImage(@PathVariable("id") Long id) {
        VillageImageDTO villageImageDTO = villageImageService.getVillageImageById(id);
        return ResponseEntity.ok(villageImageService.rejectSingleVillageImage(villageImageDTO, LocalDateTime.now()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillageImageDTO> getVillageImageById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(villageImageService.getVillageImageById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImageById(@PathVariable("id") Long id) {
        villageImageService.deleteImageFileById(id);
        return new ResponseEntity<>("Image with id " + id + " has been deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/resume/{id}")
    public ResponseEntity<VillageImageDTO> resumeImageVillageById(@PathVariable("id") Long id) {
        VillageImageDTO villageImageDTO = villageImageService.resumeImageById(id);
        return new ResponseEntity<>(villageImageDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VillageImageDTO> updateVillageImage(@PathVariable("id") Long id, @Valid @RequestBody VillageImageDTO villageImageDTO) {
        VillageImageDTO updatedVillageImage = villageImageService.updateVillageImage(id, villageImageDTO);
        return ResponseEntity.ok(updatedVillageImage);
    }
    @GetMapping("/upload-images")
    public ResponseEntity<String> uploadImages() {
        try {
            System.out.println("upload images controller backend");
            villageImageService.uploadImages();
            return ResponseEntity.ok("Images uploaded successfully.");
        } catch (Exception ex) {
            return ResponseEntity.ok("Image upload failed: " + ex.getMessage());
        }
    }
}
