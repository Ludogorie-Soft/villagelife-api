package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.services.AdminVillageService;
import com.example.ludogorieSoft.village.services.PopulationService;
import com.example.ludogorieSoft.village.services.VillageInfoService;
import com.example.ludogorieSoft.village.services.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/functions")
@RequiredArgsConstructor
public class AdminFunctionController {
    private final VillageService villageService;
    private final AdminVillageService adminVillageService;
    private final VillageInfoService villageInfoService;
    private PopulationService populationService;

    @DeleteMapping("/village-delete/{villageId}")
    public ResponseEntity<String> deleteVillageById(@PathVariable("villageId") Long villageId) {
        villageService.deleteVillage(villageId);
        return new ResponseEntity<>("Village with id: " + villageId + " has been deleted successfully!!", HttpStatus.OK);
    }

    @PostMapping("/approve/{villageId}")
    public ResponseEntity<String> approveVillageResponse(@RequestParam("villageId") Long villageId,
                                                         @RequestParam("answerDate") String answerDate) {
        VillageDTO villageDTO = villageService.getVillageById(villageId);
        villageService.updateVillageStatus(villageId, villageDTO);
        adminVillageService.updateVillageStatusAndVillageResponsesStatus(villageId, answerDate);
        return new ResponseEntity<>("Status of village with ID: " + villageId + " changed successfully!!!", HttpStatus.OK);
    }

    @GetMapping("/toApprove")
    public ResponseEntity<List<VillageResponse>> getUnapprovedVillageResponses() {
        List<VillageResponse> villageResponse = adminVillageService.getUnapprovedVillageResponsesWithSortedAnswers(false);
        return new ResponseEntity<>(villageResponse, HttpStatus.OK);
    }

    @PostMapping("/reject/{villageId}")
    public ResponseEntity<String> rejectVillageResponse(@RequestParam("villageId") Long villageId,
                                                        @RequestParam("answerDate") String answerDate) {
        adminVillageService.rejectVillageResponses(villageId, answerDate);
        return new ResponseEntity<>("Response of village with ID: " + villageId + " rejected successfully!!!", HttpStatus.OK);
    }

    @GetMapping("/info/{villageId}")
    public ResponseEntity<VillageInfo> getVillageInfoById(@RequestParam("villageId") Long villageId,
                                                          @RequestParam("answerDate") String answerDate, boolean status) {
        VillageInfo villageInfo = villageInfoService.getVillageInfoByVillageId(villageId, status, answerDate);
        return ResponseEntity.ok(villageInfo);
    }
    @GetMapping("/population/villageId/{id}")
    public ResponseEntity<PopulationDTO> getPopulationByVillageId(@PathVariable("id") Long id,
                                                                  @RequestParam("answerDate") String answerDate, boolean status) {
        return ResponseEntity.ok(populationService.getPopulationByVillageId(id, status, answerDate));
    }
    @GetMapping("/getRejected")
    public ResponseEntity<List<VillageResponse>> getVillagesWithRejectedResponses() {
        List<VillageResponse> villageResponse = adminVillageService.getRejectedVillageResponsesWithSortedAnswers(false);
        return new ResponseEntity<>(villageResponse, HttpStatus.OK);
    }

}
