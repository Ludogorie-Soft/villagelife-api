package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.UserSearchDataDTO;
import com.example.ludogorieSoft.village.services.UserSearchDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/get-all-for-user")
    public ResponseEntity<List<UserSearchDataDTO>> getAllUserSearchDataDTOsForUser(@RequestParam(value = "alternativeUserId", required = false) Long id) {
        List<UserSearchDataDTO> userSearchDataDTOList = userSearchDataService.getAllUserSearchDataDTOsForUser(id);
        return ResponseEntity.ok(userSearchDataDTOList);
    }
}
