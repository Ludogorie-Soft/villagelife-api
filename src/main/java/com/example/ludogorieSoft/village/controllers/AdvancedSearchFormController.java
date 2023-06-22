package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.AdvancedSearchForm;
import com.example.ludogoriesoft.village.services.AdvancedSearchFormService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/advancedSearchForm")
@AllArgsConstructor
public class AdvancedSearchFormController {
    private final AdvancedSearchFormService advancedSearchFormService;
    @PostMapping
    public ResponseEntity<AdvancedSearchForm> createAdvancedSearchFormResult(@Valid @RequestBody AdvancedSearchForm AdvancedSearchForm) {
        AdvancedSearchForm createdAdvancedSearchForm = advancedSearchFormService.create(AdvancedSearchForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdvancedSearchForm);
    }
}
