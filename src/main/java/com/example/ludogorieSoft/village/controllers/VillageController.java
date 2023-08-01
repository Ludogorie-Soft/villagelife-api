package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.services.AdminVillageService;
import com.example.ludogorieSoft.village.services.VillageInfoService;
import com.example.ludogorieSoft.village.services.VillageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villages")
@AllArgsConstructor
public class VillageController {

    private final VillageService villageService;
    private final VillageInfoService villageInfoService;
    private final AdminVillageService adminVillageService;

    @GetMapping
    public ResponseEntity<List<VillageDTO>> getAllVillages() {
        List<VillageDTO> villages = villageService.getAllVillages();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillageDTO> getVillageById(@PathVariable("id") Long id) {
        VillageDTO village = villageService.getVillageById(id);
        return ResponseEntity.ok(village);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<VillageInfo> getVillageInfoById(@PathVariable("id") Long id) {
        VillageInfo villageInfo = villageInfoService.getVillageInfoByVillageId(id);
        return ResponseEntity.ok(villageInfo);
    }

    @PostMapping
    public ResponseEntity<VillageDTO> createVillage(@Valid @RequestBody VillageDTO villageDTO) {
        VillageDTO createdVillage = villageService.createVillage(villageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVillage);
    }

    @PostMapping("/null")
    public ResponseEntity<Long> createVillageWithNullValues() {
        Long villageID = villageService.createVillageWhitNullValues();
        return ResponseEntity.status(HttpStatus.OK).body(villageID);
    }


    @PutMapping("/{id}")
    public ResponseEntity<VillageDTO> updateVillage(@PathVariable("id") Long id, @Valid @RequestBody VillageDTO villageDTO) {
        VillageDTO updatedVillage = villageService.updateVillage(id, villageDTO);
        return ResponseEntity.ok(updatedVillage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVillage(@PathVariable("id") Long id) {
        villageService.deleteVillage(id);
        return ResponseEntity.noContent().build();
    }

    //    @GetMapping("/update/{villageId}")
//    public ResponseEntity<VillageDTO> findVillageById(@PathVariable(name = "villageId") Long id) {
//        VillageDTO village = villageService.getVillageById(id);
//
//        return new ResponseEntity<>(village,HttpStatus.OK);
//    }
    @GetMapping("/update")
    public  ResponseEntity<List<VillageResponse>> findUnapprovedVillageResponseByVillageId() {
        List<VillageResponse> villageResponse = adminVillageService.getUnapprovedVillageResponsesWithSortedAnswers(false);
        return new ResponseEntity<>(villageResponse, HttpStatus.OK);
    }

}
