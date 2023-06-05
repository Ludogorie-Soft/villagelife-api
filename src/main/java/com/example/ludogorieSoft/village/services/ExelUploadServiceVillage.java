package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Village;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExelUploadServiceVillage {
    public class ExcelUploadService {
        public static boolean isValidExcelFile(MultipartFile file){
            return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
        }
        public static List<Village> getCustomersDataFromExcel(InputStream inputStream){
            List<Village> villages = new ArrayList<>();
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheet("vilage");
                int rowIndex =0;
                for (Row row : sheet){
                    if (rowIndex ==0){
                        rowIndex++;
                        continue;
                    }
                    Iterator<Cell> cellIterator = row.iterator();
                    int cellIndex = 0;
                    Village village = new Village();
                    while (cellIterator.hasNext()){
                        Cell cell = cellIterator.next();
                        switch (cellIndex){
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

    }}
