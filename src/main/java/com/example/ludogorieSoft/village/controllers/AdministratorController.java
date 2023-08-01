package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.model.EthnicityVillage;
import com.example.ludogorieSoft.village.repositories.EthnicityVillageRepository;
import com.example.ludogorieSoft.village.services.AdminVillageService;
import com.example.ludogorieSoft.village.services.AdministratorService;
import com.example.ludogorieSoft.village.services.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdministratorController {
    private final AdministratorService administratorService;
    private final VillageService villageService;
    private final AdminVillageService adminVillageService;
    private final EthnicityVillageRepository ethnicityVillageRepository;

    @GetMapping
    public ResponseEntity<List<AdministratorDTO>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorDTO> getAdministratorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(administratorService.getAdministratorById(id));
    }


    @PostMapping
    public ResponseEntity<AdministratorDTO> createAdministrator(@Valid @RequestBody AdministratorRequest administratorRequest) {
        return ResponseEntity.ok(administratorService.createAdministrator(administratorRequest));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<AdministratorDTO> updateAdministrator(@PathVariable("id") Long id, @Valid @RequestBody AdministratorRequest administratorRequest) {
        return ResponseEntity.ok(administratorService.updateAdministrator(id, administratorRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdministratorById(@PathVariable("id") Long id) {
        administratorService.deleteAdministratorById(id);
        return new ResponseEntity<>("Administrator with id: " + id + " has been deleted successfully!!", HttpStatus.OK);
    }

    @GetMapping("village")
    public List<VillageResponse> getAllVillages() {

        return villageService.getAllVillagesWithAdmin();//administratorService.getAllVillagesWithPopulation()
    }

    @DeleteMapping("/village-delete/{villageId}")
    public ResponseEntity<String> deleteVillageById(@PathVariable("villageId") Long villageId) {
        villageService.deleteVillage(villageId);
        return new ResponseEntity<>("Village with id: " + villageId + " has been deleted successfully!!", HttpStatus.OK);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> changeVillageStatus(@PathVariable(name = "id") Long id) {
        VillageDTO villageDTO = villageService.getVillageById(id);
        villageService.updateVillageStatus(id, villageDTO);
        return new ResponseEntity<>("Status of village with ID: " + id + " changed successfully!!!", HttpStatus.OK);
    }

    @GetMapping("/update/{villageId}")
    public ResponseEntity<VillageDTO> findVillageById(@PathVariable(name = "villageId") Long id) {
        VillageDTO village = villageService.getVillageById(id);

        return new ResponseEntity<>(village,HttpStatus.OK);
    }
    @GetMapping("/update")
    public  ResponseEntity<List<VillageResponse>> findUnapprovedVillageResponseByVillageId() {
        List<VillageResponse> villageResponse = adminVillageService.getUnapprovedVillageResponsesWithSortedAnswers(false);
        return new ResponseEntity<>(villageResponse, HttpStatus.OK);
    }
    @PostMapping("/approve-answer/{villageId}")
    public void getEthnisity(@RequestParam("villageId") Long villageId,
                             @RequestParam("answerDate") String answerDate){
        String dateTimeString = answerDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        boolean status = false;
        List<EthnicityVillage> ethnicityVillage = ethnicityVillageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId,status,localDateTime);

        System.out.println("ethnisity " + ethnicityVillage);

    }
}
