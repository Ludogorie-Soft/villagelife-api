package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.services.TranslatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/translator")
@AllArgsConstructor
public class TranslatorController {
    private final TranslatorService translatorService;
    @GetMapping("/toLatin/{cyrillicName}")
    public ResponseEntity<String> translateToLatin(@PathVariable String cyrillicName){
        return ResponseEntity.ok(translatorService.translateToLatin(cyrillicName));
    }
}
