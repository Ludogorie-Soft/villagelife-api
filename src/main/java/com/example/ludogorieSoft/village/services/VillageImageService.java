package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
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
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class VillageImageService {
    private final VillageImageRepository villageImageRepository;
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/village_images/";
    private final ModelMapper modelMapper;
    private final VillageService villageService;
    private static final String ERROR_MESSAGE = "An error occurred while processing the image";
    private static final String VILLAGE_IMAGE_ID_MESSAGE = "VillageImage with id: ";
    private static final String NOT_FOUND_MESSAGE = " not found";
    private static final Logger logger = LoggerFactory.getLogger(VillageImageService.class);

    public List<String> createImagePaths(List<byte[]> imageBytes, Long villageId, LocalDateTime localDateTime, Boolean status) { //ddd
        List<String> imagePaths = new ArrayList<>();
        for (byte[] image : imageBytes) {
            if (image.length > 0) {
                String imagePath = processImage(image);
                if (imagePath != null) {
                    imagePaths.add(imagePath);
                    createVillageImageDTO(villageId, imagePath,localDateTime, status);
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
            logger.error(ERROR_MESSAGE, e);
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

    public void createVillageImageDTO(Long villageId, String fileName, LocalDateTime localDateTime, boolean status) {//ddd
        VillageImageDTO villageImageDTO = new VillageImageDTO(null, villageId, fileName, status, localDateTime,null, null);//added false for status column /ddd
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
        villageImage.setDateUpload(villageImageDTO.getDateUpload());

        villageImageRepository.save(villageImage);

        return villageImageDTO;
    }
    public VillageImage villageImageDTOToVillageImage(VillageImageDTO villageImageDTO) {
        return modelMapper.map(villageImageDTO, VillageImage.class);
    }
    public VillageImageDTO villageImageToVillageImageDTO(VillageImage villageImage) {
        return modelMapper.map(villageImage, VillageImageDTO.class);
    }
    public List<String> getAllImagesForVillageByStatusAndDate(Long villageId, boolean status, String date) {
        List<String> base64Images = new ArrayList<>();
        List<VillageImage> villageImages;
        if(status){
            villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, true);
        }else {
            villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, false ,date);

        }
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
                logger.error(ERROR_MESSAGE, e);
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

    public List<VillageDTO> getAllApprovedVillageDTOsWithImages() {
        List<VillageDTO> villageDTOsWithImages = new ArrayList<>();
        List<VillageDTO> allVillageDTOs = villageService.getVillagesByStatus(true);

        for (VillageDTO village : allVillageDTOs) {
            List<String> images = getAllImagesForVillageByStatusAndDate(village.getId(),true,null);
            village.setImages(images);
            villageDTOsWithImages.add(village);
        }

        return villageDTOsWithImages;
    }

    public void updateVillageImagesStatus(Long id, boolean status, String localDateTime) {
        List<VillageImage> villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, localDateTime);

        if (!villageImages.isEmpty()) {
            for (VillageImage villageImage : villageImages) {
                villageImage.setVillageStatus(!status);
                villageImage.setDateDeleted(null);
                updateVillageImage(villageImage.getId(), villageImageToVillageImageDTO(villageImage));
            }
        }
    }

    public VillageImageDTO updateVillageImage(Long id, VillageImageDTO villageImageDTO) {
        Optional<VillageImage> villageImage = villageImageRepository.findById(id);
        if(villageImage.isEmpty()){
            throw new ApiRequestException(VILLAGE_IMAGE_ID_MESSAGE + id + NOT_FOUND_MESSAGE);
        }
        villageImage.get().setVillage(villageService.checkVillage(villageImageDTO.getVillageId()));
        villageImage.get().setImageName(villageImageDTO.getImageName());
        villageImage.get().setVillageStatus(villageImageDTO.getStatus());
        villageImage.get().setDateUpload(villageImageDTO.getDateUpload());
        villageImage.get().setDateDeleted(villageImageDTO.getDateDeleted());
        return villageImageToVillageImageDTO(villageImageRepository.save(villageImage.get()));
    }

    public void rejectVillageImages(Long id, boolean status, String responseDate,LocalDateTime dateDelete) {
        List<VillageImage> villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(
                id, status, responseDate
        );
        if (!villageImages.isEmpty()) {
            for (VillageImage vill : villageImages) {
                rejectSingleVillageImage(villageImageToVillageImageDTO(vill), dateDelete);
            }
        }
    }



    public VillageImageDTO rejectSingleVillageImage(VillageImageDTO villageImage, LocalDateTime dateDelete){
        villageService.checkVillage(villageImage.getVillageId());
        villageImage.setDateDeleted(dateDelete);
        villageImageRepository.save(villageImageDTOToVillageImage(villageImage));
        return villageImage;
    }
    public List<VillageImageDTO> getNotDeletedVillageImageDTOsByVillageId(Long villageId){
        List<VillageImage> villageImages = villageImageRepository.findNotDeletedByVillageId(villageId);
        return getVillageImageDTOsByVillageId(villageImages);
    }
    public List<VillageImageDTO> getDeletedVillageImageDTOsByVillageId(Long villageId){
        List<VillageImage> villageImages = villageImageRepository.findDeletedByVillageId(villageId);
        return getVillageImageDTOsByVillageId(villageImages);
    }
    public List<VillageImageDTO> getVillageImageDTOsByVillageId(List<VillageImage> villageImages){
        List<VillageImageDTO> villageImagesWithBase64Images = new ArrayList<>();
        for (VillageImage villageImage : villageImages) {
            VillageImageDTO villageImageDTO = villageImageToVillageImageDTO(villageImage);
            String imagePath = UPLOAD_DIRECTORY + villageImage.getImageName();
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    byte[] imageBytes = readImageBytes(imageFile);
                    String base64Image = encodeImageToBase64(imageBytes);
                    villageImageDTO.setBase64Image(base64Image);
                }
            } catch (IOException e) {
                logger.error(ERROR_MESSAGE, e);
            }
            villageImagesWithBase64Images.add(villageImageDTO);
        }
        return villageImagesWithBase64Images;
    }

    public VillageImageDTO getVillageImageById(Long id){
        Optional<VillageImage> foundVillageImage = villageImageRepository.findById(id);
        if(foundVillageImage.isPresent()){
            return villageImageToVillageImageDTO(foundVillageImage.get());
        }
        throw new ApiRequestException(VILLAGE_IMAGE_ID_MESSAGE + id + NOT_FOUND_MESSAGE);
    }


    public void deleteImageFileById(Long id) {
        VillageImageDTO villageImageDTO = getVillageImageById(id);

        if (villageImageDTO != null) {
            String imageName = villageImageDTO.getImageName();
            Path imagePath = Paths.get(UPLOAD_DIRECTORY, imageName);
            try {
                Files.delete(imagePath);
                deleteVillageImageById(id);
            } catch (NoSuchFileException e) {
                logger.warn("Image does not exist: {}", imageName);
            } catch (DirectoryNotEmptyException e) {
                logger.error("Cannot delete non-empty directory: {}", imageName);
            } catch (IOException e) {
                logger.error("Failed to delete image: {}", imageName, e);
            }
        } else {
            logger.warn("Image with ID {} not found.", id);
        }
    }

    public void deleteVillageImageById(Long id) {
        if (villageImageRepository.existsById(id)) {
            villageImageRepository.deleteById(id);
        } else {
            throw new ApiRequestException(VILLAGE_IMAGE_ID_MESSAGE + id + NOT_FOUND_MESSAGE);
        }
    }

    public VillageImageDTO resumeImageById(Long id){
        Optional<VillageImage> villageImage = villageImageRepository.findById(id);
        if (villageImage.isPresent()){
            villageImage.get().setDateDeleted(null);
            return villageImageToVillageImageDTO(villageImageRepository.save(villageImage.get()));
        }
        throw new ApiRequestException(VILLAGE_IMAGE_ID_MESSAGE + id + NOT_FOUND_MESSAGE);
    }
}
