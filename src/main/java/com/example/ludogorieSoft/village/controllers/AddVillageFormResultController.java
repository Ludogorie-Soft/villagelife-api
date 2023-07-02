package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AddVillageFormResult;
import com.example.ludogorieSoft.village.services.AddVillageFormResultService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/addVillageForm")
@AllArgsConstructor
public class AddVillageFormResultController {
    private final AddVillageFormResultService addVillageFormResultService;
    @PostMapping
    public ResponseEntity<AddVillageFormResult> createAddVillageForResult(@Valid @RequestBody AddVillageFormResult addVillageFormResult) {
        AddVillageFormResult createdAddVillageFormResult = addVillageFormResultService.create(addVillageFormResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddVillageFormResult);
    }
}
