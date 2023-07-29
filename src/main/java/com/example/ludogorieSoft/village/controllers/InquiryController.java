package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.InquiryDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.InquiryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/inquiries")
@AllArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;
    @PostMapping
    public ResponseEntity<InquiryDTO> createInquiry(@Valid @RequestBody InquiryDTO inquiryDTO) {
        try {
            InquiryDTO createdInquiryDTO = inquiryService.createInquiry(inquiryDTO);
            return new ResponseEntity<>(createdInquiryDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ApiRequestException("Error creating inquiry");
        }
    }
}
