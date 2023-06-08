package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.services.ExcelToDBService;
import com.example.ludogorieSoft.village.services.VillageService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/villages")
@AllArgsConstructor
public class VillageController {

    private final VillageService villageService;
    private final ExcelToDBService excelToDBService;

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

    @PostMapping
    public ResponseEntity<VillageDTO> createVillage(@RequestBody VillageDTO villageDTO) {
        VillageDTO createdVillage = villageService.createVillage(villageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVillage);
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
    @PostMapping("/upload-villages-data")
    public ResponseEntity<?> uploadVillagesData(@RequestParam("file") MultipartFile file){
        this.excelToDBService.saveVillagesToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message", " Customers data uploaded and saved to database successfully"));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        File excelFile = new File("C:/Users/ACER/Desktop/VL surveys/Village_Life_Listing2023-05-19_13_26_13.xlsx");

        try {
            FileInputStream fis = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Изберете листа, който желаете да обработите

            // Обход на всички редове в листа
            for (Row row : sheet) {
                // Взимане на стойностите от съответните клетки на реда
                Cell submissionDateCell = row.getCell(0);
                Cell villageAndMunicipalityCell = row.getCell(1);
                Cell supermarketDistanceCell = row.getCell(2);
                // ... и т.н. - продължете за всички нужни клетки

                String submissionDate = submissionDateCell.getStringCellValue();
                String villageAndMunicipality = villageAndMunicipalityCell.getStringCellValue();
                String supermarketDistance = supermarketDistanceCell.getStringCellValue();

                // Направете необходимата обработка на данните
                // Например, можете да ги изпишете в текстов файл или да ги запазите в база данни

//                System.out.println("Submission Date: " + submissionDate);
//                System.out.println("Village and Municipality: " + villageAndMunicipality);
//                System.out.println("Supermarket Distance: " + supermarketDistance);
            }
            workbook.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("YESSSSS");
    }
}
