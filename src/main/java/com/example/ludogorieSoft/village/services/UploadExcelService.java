package com.example.ludogorieSoft.village.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadExcelService {
    public Long uploadFile(MultipartFile file) {
        for (int i = 0; i <2000000 ; i++) {
            System.err.println("UploadExcelService.uploadFile");
        }
        try {
            if (!file.getOriginalFilename().endsWith(".xlsx")) {
                return 0L;
            } else return 1L;
        }catch (Exception e){
            return 0L;
        }
    }
}
