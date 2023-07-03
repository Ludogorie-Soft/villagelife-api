package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.services.UploadExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadExcelController {

    private final UploadExcelService uploadExcelService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadFile(@RequestParam("file") MultipartFile file) {
        Long addedVillageNumber = uploadExcelService.uploadFile(file);
        return ResponseEntity.ok(addedVillageNumber);
    }
}
