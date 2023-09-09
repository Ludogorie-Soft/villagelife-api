package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/validations")
@AllArgsConstructor
public class ValidationUtilsController {

    @GetMapping("/check-name")
    public ResponseEntity<Boolean> usernameCheck(@RequestParam String name) {
        return ResponseEntity.ok(ValidationUtils.isValidName(name));
    }
    @GetMapping("/check-number")
    public ResponseEntity<Boolean> numberCheck(@RequestParam String number) {
        return ResponseEntity.ok(ValidationUtils.isValidNumber(number));
    }
}
