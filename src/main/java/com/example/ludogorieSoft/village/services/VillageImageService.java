package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.repositories.VillageImageRepository;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class VillageImageService {
    private final VillageImageRepository villageImageRepository;
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/village_images/";
    private final ModelMapper modelMapper;
    private final VillageService villageService;
    private static final Logger logger = LoggerFactory.getLogger(VillageImageService.class);

    public List<String> createImagePaths(List<byte[]> imageBytes, Long villageId) {
        List<String> imagePaths = new ArrayList<>();
        for (byte[] image : imageBytes) {
            if (image.length > 0) {
                String imagePath = processImage(image);
                if (imagePath != null) {
                    imagePaths.add(imagePath);
                    createVillageImageDTO(villageId, imagePath);
                }
            }
        }
        return imagePaths;
    }

    public String processImage(byte[] image) {
        try {
            Tika tika = new Tika();
            String mimeType = tika.detect(image);
            if (!mimeType.startsWith("image/")) {
                return null;
            }
            String fileName = generateFileName();
            String filePath = getUploadDirectoryPath();
            createUploadDirectory(filePath);
            String fullPath = filePath + File.separator + fileName;
            writeImageToFile(image, fullPath);
            return fileName;
        } catch (IOException e) {
            logger.error("An error occurred while processing the image", e);
            return null;
        }
    }

    public String generateFileName() {
        return UUID.randomUUID() + ".jpg";
    }

    public void createUploadDirectory(String filePath) {
        File uploadDir = new File(filePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    public void writeImageToFile(byte[] image, String fullPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            fos.write(image);
        }
    }

    public void createVillageImageDTO(Long villageId, String fileName) {
        VillageImageDTO villageImageDTO = new VillageImageDTO(null, villageId, fileName,false,now());//added false for status column /ddd
        createVillageImageDTO(villageImageDTO);
    }

    public String getUploadDirectoryPath() {
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
    public List<String> getAllImagesForVillage(Long villageId) {
        List<String> base64Images = new ArrayList<>();
        List<VillageImage> villageImages = villageImageRepository.findByVillageId(villageId);
        if (villageImages.isEmpty()) {
            base64Images.add(null);
        } else {
            addVillageImages(base64Images, villageImages);
        }
        return base64Images;
    }

    public void addVillageImages(List<String> base64Images, List<VillageImage> villageImages) {
        for (VillageImage villageImage : villageImages) {
            String imagePath = UPLOAD_DIRECTORY + villageImage.getImageName();
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    byte[] imageBytes = readImageBytes(imageFile);
                    String base64Image = encodeImageToBase64(imageBytes);
                    base64Images.add(base64Image);
                }
            } catch (IOException e) {
                logger.error("An error occurred while processing the image", e);
            }
        }
    }

    public byte[] readImageBytes(File imageFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(imageFile);
        return IOUtils.toByteArray(inputStream);
    }

    public String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public List<VillageDTO> getAllVillageDTOsWithImages(){
        List<VillageDTO> villageDTOs = villageService.getAllVillages();
        for (VillageDTO village: villageDTOs) {
            List<String> images = getAllImagesForVillage(village.getId());
            village.setImages(images);
        }
        return villageDTOs;
    }

    public List<VillageDTO> getAllApprovedVillageDTOsWithImages() {
        List<VillageDTO> villageDTOsWithImages = new ArrayList<>();
        List<VillageDTO> allVillageDTOs = villageService.getVillagesByStatus(true);

        for (VillageDTO village : allVillageDTOs) {
            List<String> images = getAllImagesForVillage(village.getId());
            village.setImages(images);
            villageDTOsWithImages.add(village);
        }

        return villageDTOsWithImages;
    }

}
