package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.services.VillageInfoService;
import com.example.ludogorieSoft.village.services.VillageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/villages")
@AllArgsConstructor
public class VillageController {

    private final VillageService villageService;
    private final VillageInfoService villageInfoService;


    @GetMapping("/{id}")
    public ResponseEntity<VillageDTO> getVillageById(@PathVariable("id") Long id) {
        VillageDTO village = villageService.getVillageById(id);
        return ResponseEntity.ok(village);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<VillageInfo> getVillageInfoById(@PathVariable("id") Long id) {
        boolean status = true;
        String answerDate = null;
        VillageInfo villageInfo = villageInfoService.getVillageInfoByVillageId(id,status,answerDate);
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

    @PutMapping("/{id}/increase-approved-responses-count")
    public void increaseApprovedResponsesCount(@PathVariable Long id) {
        villageService.increaseApprovedResponsesCount(id);
    }
    @GetMapping("/name/{key}")
    public VillageDTO findVillageByNameAndRegion(@PathVariable String key) {
       return villageService.getVillageByNameAndRegionName(key);
    }
    @GetMapping("/status/{status}")
    public List<Long> testSitemap(@PathVariable boolean status) {
        return villageService.getAllApprovedVillagesByStatus(status);
    }
}
