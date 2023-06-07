package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.*;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor

public class ExcelToDBService {
    private final VillageRepository villageRepository;
    private final AdministratorRepository administratorRepository;
    private final EthnicityRepository ethnicityRepository;
    private final EthnicityVillageRepository ethnicityVillageRepository;
    private final GroundCategoryRepository groundCategoryRepository;
    private final LandscapeRepository landscapeRepository;
    private final LivingConditionRepository livingConditionRepository;
    private final ObjectAroundVillageRepository objectAroundVillageRepository;
    private final ObjectVillageRepository objectVillageRepository;
    private final PopulatedAssertionRepository populatedAssertionRepository;
    private final PopulationRepository populationRepository;
    private final QuestionRepository questionRepository;
    private final VillageAnswerQuestionRepository villageAnswerQuestionRepository;
    private final VillageGroundCategoryRepository villageGroundCategoryRepository;
    private final VillageLandscapeRepository villageLandscapeRepository;
    private final VillageLivingConditionRepository villageLivingConditionRepository;
    private final VillagePopulationAssertionRepository villagePopulationAssertionRepository;

    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Village> getVillageDataFromExcel(InputStream inputStream) {
        List<Village> villages = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("vilage");
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Village village = new Village();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0 -> village.setId((long) cell.getNumericCellValue());
                        case 1 -> village.setName(cell.getStringCellValue());
//                            case 2 -> village.setPopulation((int) cell.getNumericCellValue());
//                            case 3 -> village.setDateUpload(cell.getLocalDateTimeCellValue());
//                            case 4 -> village.setStatus( cell.getBooleanCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                villages.add(village);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return villages;
    }


    public void saveVillagesToDatabase(MultipartFile file){
        if(isValidExcelFile(file)){
            try {
                List<Village> customers = getVillageDataFromExcel(file.getInputStream());
                this.villageRepository.saveAll(customers);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }
}
