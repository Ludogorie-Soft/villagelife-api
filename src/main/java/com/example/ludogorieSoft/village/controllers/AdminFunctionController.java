package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageVideoDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.services.*;
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
    private final VillageImageService villageImageService;
    private final VillageVideoService villageVideoService;

    @DeleteMapping("/village-delete/{villageId}")
    public ResponseEntity<String> deleteVillageById(@PathVariable("villageId") Long villageId) {
        villageImageService.deleteAllImageFilesByVillageId(villageId);
        villageService.deleteVillage(villageId);
        return new ResponseEntity<>("Village with id: " + villageId + " has been deleted successfully!!", HttpStatus.OK);
    }

    @PostMapping("/approve/{villageId}")
    public ResponseEntity<String> approveVillageResponse(@PathVariable("villageId") Long villageId,
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
    public ResponseEntity<String> rejectVillageResponse(@PathVariable("villageId") Long villageId,
                                                        @RequestParam("answerDate") String answerDate) {
        adminVillageService.rejectVillageResponses(villageId, answerDate);
        return new ResponseEntity<>("Response of village with ID: " + villageId + " rejected successfully!!!", HttpStatus.OK);
    }

    @GetMapping("/info/{villageId}")
    public ResponseEntity<VillageInfo> getVillageInfoById(@PathVariable("villageId") Long villageId,
                                                          @RequestParam("answerDate") String answerDate, boolean status) {
        VillageInfo villageInfo = villageInfoService.getVillageInfoByVillageId(villageId, status, answerDate);
        return ResponseEntity.ok(villageInfo);
    }

    @GetMapping("/getRejected")
    public ResponseEntity<List<VillageResponse>> getVillagesWithRejectedResponses() {
        List<VillageResponse> villageResponse = adminVillageService.getRejectedVillageResponsesWithSortedAnswers(false);
        return new ResponseEntity<>(villageResponse, HttpStatus.OK);
    }

    @GetMapping("/toLatin")
    public ResponseEntity<String> translateVillagesNamesToLatin() {
        try {
            villageService.translateVillagesNames();
            return ResponseEntity.ok("Village names have been translated successfully");
        } catch (Exception ex) {
            return new ResponseEntity<>("Translation failed: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/video")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveVideos(@RequestParam("villageId") Long villageId, @RequestParam("videoUrl") List<String> villageVideoDTOS) {
        villageVideoService.createVideoPats(villageId, villageVideoDTOS);
        return "Video successfully saved";
    }

    @GetMapping("/videos/{villageId}")
    public List<VillageVideoDTO> getAllVideosByVillageId(@PathVariable(value = "villageId") Long villageId) {
        return villageVideoService.findVideosByVillageIdAndDateDeletedIsNull(villageId);
    }

    @GetMapping("/deleted-videos/{villageId}")
    public List<VillageVideoDTO> getAllDeletedVideosByVillageId(@PathVariable(value = "villageId") Long villageId) {
        return villageVideoService.findDeletedVideosByVillageIdAndDateDeletedIsNotNull(villageId);
    }

    @DeleteMapping("/video-delete/{videoId}")
    public String deleteVideoByVideoId(@PathVariable(value = "videoId") Long videoId) {
        return villageVideoService.deleteVideoById(videoId);
    }

    @PutMapping("/resume-video/{videoId}")
    public String resumeVideoByVideoId(@PathVariable(value = "videoId") Long videoId) {
        return villageVideoService.resumeVideoById(videoId);
    }

    @PutMapping("/reject-video/{videoId}")
    public String rejectVideoByVideoId(@PathVariable(value = "videoId") Long videoId) {
        return villageVideoService.rejectVideoById(videoId);
    }
}
