package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.UserSearchDataDTO;
import com.example.ludogorieSoft.village.services.UserSearchDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-search-data")
@RequiredArgsConstructor
public class UserSearchDataController {
    private final UserSearchDataService userSearchDataService;

    @PostMapping
    public ResponseEntity<UserSearchDataDTO> createUserSearchData(@Valid @RequestBody UserSearchDataDTO userSearchDataDTO) {
        UserSearchDataDTO createdUserSearchDataDTO = userSearchDataService.createUserSearchData(userSearchDataDTO);
        return new ResponseEntity<>(createdUserSearchDataDTO, HttpStatus.CREATED);
    }
}
