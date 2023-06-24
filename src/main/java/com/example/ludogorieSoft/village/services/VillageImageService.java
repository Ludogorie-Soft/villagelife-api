package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.VillageImageDTO;
import com.example.ludogoriesoft.village.model.Village;
import com.example.ludogoriesoft.village.model.VillageImage;
import com.example.ludogoriesoft.village.repositories.VillageImageRepository;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VillageImageService {
    private final VillageImageRepository villageImageRepository;
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/village_images";
    private final ModelMapper modelMapper;
    private final VillageService villageService;

    public List<String> createImagePaths(List<byte[]> imageBytes, Long villageId) {
        List<String> imagePaths = new ArrayList<>();
        for (byte[] image : imageBytes) {
            if (image.length > 0) {
                try {
                    Tika tika = new Tika();
                    String mimeType = tika.detect(image);
                    if (!mimeType.startsWith("image/")) {
                        imagePaths.add("Invalid file format. Only images are allowed.");
                        continue;
                    }
                    String fileName = UUID.randomUUID() + ".jpg";
                    String filePath = getUploadDirectoryPath();
                    File uploadDir = new File(filePath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    String fullPath = filePath + File.separator + fileName;
                    FileOutputStream fos = new FileOutputStream(fullPath);
                    fos.write(image);
                    fos.close();
                    imagePaths.add(fileName);

                    VillageImageDTO villageImageDTO = new VillageImageDTO(null, villageId, fileName);
                    createVillageImageDTO(villageImageDTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imagePaths;
    }
    private String getUploadDirectoryPath() {
        String currentPath = System.getProperty("user.dir");
        return currentPath + File.separator + UPLOAD_DIRECTORY;
    }

    public VillageImageDTO createVillageImageDTO(VillageImageDTO villageImageDTO) {
        VillageImage villageImage = villageImageDTOToVillageImage(villageImageDTO);
        Village village = villageService.checkVillage(villageImageDTO.getVillageId());
        villageImage.setVillage(village);
        villageImageRepository.save(villageImage);
        return villageImageDTO;
    }
    public VillageImage villageImageDTOToVillageImage(VillageImageDTO villageImageDTO) {
        return modelMapper.map(villageImageDTO, VillageImage.class);
    }
    public List<byte[]> getImageBytesFromMultipartFile(List<MultipartFile> images){
        List<byte[]> imageBytes = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                byte[] imageData = image.getBytes();
                imageBytes.add(imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageBytes;
    }
}
