package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Distance;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.model.ObjectVillage;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.ObjectAroundVillageRepository;
import com.example.ludogorieSoft.village.repositories.ObjectVillageRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import lombok.AllArgsConstructor;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VillageService {

    private final VillageRepository villageRepository;
    private final ObjectVillageRepository objectVillageRepository;
    private final ObjectAroundVillageRepository objectAroundVillageRepository;
    private final ModelMapper modelMapper;


    public VillageDTO villageToVillageDTO(Village village){
        return modelMapper.map(village, VillageDTO.class);
    }

    public List<VillageDTO> getAllVillages() {
        List<Village> villages = villageRepository.findAll();
        return villages.stream()
                .map(this::villageToVillageDTO)
                .collect(Collectors.toList());
    }


    public VillageDTO getVillageById(Long id) {
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            return villageToVillageDTO(optionalVillage.get());
        } else {
            throw new ApiRequestException("Village with id " + id + " not found");
        }
    }


    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = modelMapper.map(villageDTO, Village.class);
        villageRepository.save(village);
        return modelMapper.map(village, VillageDTO.class);
    }


    public VillageDTO updateVillage(Long id, VillageDTO villageDTO) {
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            Village village = optionalVillage.get();
            village.setName(villageDTO.getName());
            village.setPopulation(villageDTO.getPopulation());
            villageRepository.save(village);
            return modelMapper.map(village, VillageDTO.class);
        } else {
            throw new ApiRequestException("Village with id " + id + " not found");
        }
    }


    public void deleteVillage(Long id) {
        if (villageRepository.existsById(id)) {
            villageRepository.deleteById(id);
        } else {
            throw new ApiRequestException("Village with id " + id + " not found");
        }
    }

        public static boolean isValidExcelFile(MultipartFile file) {
            return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }

        public  List<Village> getVillageDataFromExcel(InputStream inputStream) {
            List<Village> villages = new ArrayList<>();
            List<ObjectAroundVillage> objectList =objectAroundVillageRepository.findAll();

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
                    ObjectVillage objectVillage=new ObjectVillage();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cellIndex) {
//                            case 1 -> village.setName(cell.getStringCellValue());
//                            case 2 -> {
//                                objectVillage.setObject(objectList.get(1));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 3 -> {
//                                objectVillage.setObject(objectList.get(2));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 4 -> {
//                                objectVillage.setObject(objectList.get(3));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 5 -> {
//                                objectVillage.setObject(objectList.get(4));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 6 -> {
//                                objectVillage.setObject(objectList.get(5));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 7 -> {
//                                objectVillage.setObject(objectList.get(6));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 8 -> {
//                                objectVillage.setObject(objectList.get(7));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 9 -> {
//                                objectVillage.setObject(objectList.get(8));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 10 -> {
//                                objectVillage.setObject(objectList.get(9));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 11 -> {
//                                objectVillage.setObject(objectList.get(10));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 12 -> {
//                                objectVillage.setObject(objectList.get(11));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 13 -> {
//                                objectVillage.setObject(objectList.get(12));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 14 -> {
//                                objectVillage.setObject(objectList.get(13));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
//                            case 15 -> {
//                                objectVillage.setObject(objectList.get(14));
//                                objectVillage.setDistance(Distance.valueOf(cell.getStringCellValue()));
//                                objectVillageRepository.save(objectVillage);
//                            }
                            default -> {
                                for (int i = 0; i <44 ; i++) {
                                    System.out.println(cell.getStringCellValue());
                                }
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
