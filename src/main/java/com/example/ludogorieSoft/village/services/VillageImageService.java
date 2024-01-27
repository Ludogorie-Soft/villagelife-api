package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.UserDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.User;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.repositories.VillageImageRepository;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class VillageImageService {
    private final VillageImageRepository villageImageRepository;
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/village_images/";
    private final ModelMapper modelMapper;
    private final VillageService villageService;
    private final UserService userService;
    private static final String ERROR_MESSAGE = "An error occurred while processing the image";
    private static final String VILLAGE_IMAGE_ID_MESSAGE = "VillageImage with id: ";
    private static final String NOT_FOUND_MESSAGE = " not found";
    private static final Logger logger = LoggerFactory.getLogger(VillageImageService.class);
    private final ImageService imageService;

    ///original method
//    public List<String> createImagePaths(List<byte[]> imageBytes, Long villageId, LocalDateTime localDateTime, Boolean status, UserDTO userDTO) { //ddd
//        List<String> imagePaths = new ArrayList<>();
//        for (byte[] image : imageBytes) {
//            if (image.length > 0) {
//                String imagePath = processImage(image);
//                if (imagePath != null) {
//                    imagePaths.add(imagePath);
//                    createVillageImageDTO(villageId, imagePath, localDateTime, status, userDTO);
//                }
//            }
//        }
//        return imagePaths;
//    }
    public List<String> createImagePaths(List<byte[]> imageBytes, Long villageId, LocalDateTime localDateTime, Boolean status, UserDTO userDTO) { //ddd
        List<String> imagePaths = new ArrayList<>();

        for (byte[] image : imageBytes) {
            if (imageBytes.size() > 0) {
                String imageUUID= randomUUID().toString();
                String imageName = imageService.uploadImage(image, imageUUID);

                System.out.println("image name " + imageName);
                if (imageName != null) {
                    imagePaths.add(imageName);
                    createVillageImageDTO(villageId, imageName, localDateTime, status, userDTO);
                }
            }
        }


        return imagePaths;
    }

    public void createImagePaths2222(List<String> imageUUID, long villageId, LocalDateTime localDateTime, boolean status, UserDTO userDTO) {
        for (String image : imageUUID) {
            createVillageImageDTO(villageId, image, localDateTime, status, userDTO);
        }
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
        return randomUUID() + ".jpg";
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

    public void createVillageImageDTO(Long villageId, String fileName, LocalDateTime localDateTime, boolean status, UserDTO userDTO) {
        VillageImageDTO villageImageDTO = new VillageImageDTO(null, villageId, fileName, status, localDateTime, null, null, userDTO);
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

//    public List<String> getAllImagesForVillageByStatusAndDate(Long villageId, boolean status, String date) {
//        List<String> imagesUUID = new ArrayList<>();
//        List<VillageImage> villageImages;
//        if (status) {
//            villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, true);
//        } else {
//            villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, false, date);
//
//        }
//        if (villageImages.isEmpty()) {
//            imagesUUID.add(null);
//        } else {
//            //addVillageImages(base64Images, villageImages);
//            imagesUUID = villageImages.stream()
//                    .map(VillageImage::getImageName)
//                    .collect(toList());
//
//        }
//        return imagesUUID;
//    }
public List<String> getAllImagesForVillageByStatusAndDate(Long villageId, boolean status, String date) {
    List<String> base64Images = new ArrayList<>();
    List<VillageImage> villageImages;
    if (status) {
        villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, true);
    } else {
        villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, false, date);

    }
    if (villageImages.isEmpty()) {
        base64Images.add(null);
    } else {
        System.out.println("555555555555555" + villageImages.get(0));
        //addVillageImages(base64Images, villageImages);
        base64Images = villageImages.stream()
                    .map(villageImage -> imageService.getImageFromSpace(villageImage.getImageName())) .toList();

//        List<String> imagesResponse = Objects.requireNonNull(villageImageClient.getAllImagesForVillage(villageInfo.getVillageDTO().getId(), status, answerDate).getBody()).stream().map(imageName -> {
//            imageName = villageImageService.getImageFromSpace(imageName);
//            return imageName;
//        }).toList();
    }
    System.out.println("6666666666666" + base64Images);
    return base64Images;
}
    public void addVillageImages(List<String> base64Images, List<VillageImage> villageImages) {
        for (VillageImage villageImage : villageImages) {
            String imagePath = villageImage.getImageName();
            //try {

                File imageFile = new File(imagePath);
                if (imagePath != null) {
                   String base64Image =  imageService.getImageFromSpace(imagePath);
//                    byte[] imageBytes = readImageBytes(imageFile);
//                    String base64Image = encodeImageToBase64(imageBytes);
                    base64Images.add(base64Image);
                }
//            } catch (IOException e) {
//                logger.error(ERROR_MESSAGE, e);
//            }
        }
    }

//    public void addVillageImages(List<String> base64Images, List<VillageImage> villageImages) {
//        for (VillageImage villageImage : villageImages) {
//            String imagePath = UPLOAD_DIRECTORY + villageImage.getImageName();
//            try {
//
//                File imageFile = new File(imagePath);
//                if (imageFile.exists()) {
//                    byte[] imageBytes = readImageBytes(imageFile);
//                    String base64Image = encodeImageToBase64(imageBytes);
//                    base64Images.add(base64Image);
//                }
//            } catch (IOException e) {
//                logger.error(ERROR_MESSAGE, e);
//            }
//        }
//    }

    public byte[] readImageBytes(File imageFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(imageFile);
        return IOUtils.toByteArray(inputStream);
    }

    public String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    //    public List<VillageDTO> getAllApprovedVillageDTOsWithImages() {
//        List<VillageDTO> villageDTOsWithImages = new ArrayList<>();
//        List<VillageDTO> allVillageDTOs = villageService.getVillagesByStatus(true);
//
//        for (VillageDTO village : allVillageDTOs) {
//            List<String> images = getAllImagesForVillageByStatusAndDate(village.getId(), true, null);
//            village.setImages(images);
//            villageDTOsWithImages.add(village);
//        }
//
//        return villageDTOsWithImages;
//    }
    public Page<VillageDTO> getApprovedVillageDTOsWithImages(int pageNumber, int elementsCount) {
        Pageable page = PageRequest.of(pageNumber, elementsCount);
        Page<VillageDTO> allVillageDTOs = villageService.getVillagesByStatus(true, page);

        List<VillageDTO> villageDTOsWithImages = allVillageDTOs.getContent()
                .stream()
                .map(village -> {
                    List<String> images = getAllImagesForVillageByStatusAndDate(village.getId(), true, null);
                    village.setImages(images);
                    return village;
                })
                .collect(toList());

        return new PageImpl<>(villageDTOsWithImages, page, allVillageDTOs.getTotalElements());
    }


    public void updateVillageImagesStatus(Long id, boolean status, String localDateTime) {
        List<VillageImage> villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, localDateTime);

        if (!villageImages.isEmpty()) {
            for (VillageImage villageImage : villageImages) {
                villageImage.setVillageStatus(!status);
                villageImage.setDateDeleted(null);
                villageImage.setUser(villageImage.getUser());
                updateVillageImage(villageImage.getId(), villageImageToVillageImageDTO(villageImage));
            }
        }
    }

    public VillageImageDTO updateVillageImage(Long id, VillageImageDTO villageImageDTO) {
        Optional<VillageImage> villageImage = villageImageRepository.findById(id);
        if (villageImage.isEmpty()) {
            throw new ApiRequestException(VILLAGE_IMAGE_ID_MESSAGE + id + NOT_FOUND_MESSAGE);
        }
        villageImage.get().setVillage(villageService.checkVillage(villageImageDTO.getVillageId()));
        villageImage.get().setImageName(villageImageDTO.getImageName());
        villageImage.get().setVillageStatus(villageImageDTO.getStatus());
        villageImage.get().setDateUpload(villageImageDTO.getDateUpload());
        villageImage.get().setDateDeleted(villageImageDTO.getDateDeleted());
        return villageImageToVillageImageDTO(villageImageRepository.save(villageImage.get()));
    }

    public void rejectVillageImages(Long id, boolean status, String responseDate, LocalDateTime dateDelete) {
        List<VillageImage> villageImages = villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(
                id, status, responseDate
        );
        if (!villageImages.isEmpty()) {
            for (VillageImage vill : villageImages) {
                VillageImageDTO villageImageDTO = villageImageToVillageImageDTO(vill);
                villageImageDTO.setUserDTO(userService.mapUserToUserDTO(vill.getUser()));
                rejectSingleVillageImage(villageImageDTO, dateDelete);
            }
        }
    }

    public VillageImageDTO rejectSingleVillageImage(VillageImageDTO villageImage, LocalDateTime dateDelete) {
        villageService.checkVillage(villageImage.getVillageId());
        villageImage.setDateDeleted(dateDelete);
        villageImageRepository.save(villageImageDTOToVillageImage(villageImage));
        return villageImage;
    }

    public List<VillageImageDTO> getNotDeletedVillageImageDTOsByVillageId(Long villageId) {
        List<VillageImage> villageImages = villageImageRepository.findNotDeletedByVillageId(villageId);
        return getVillageImageDTOsByVillageId(villageImages);
    }

    public List<VillageImageDTO> getDeletedVillageImageDTOsByVillageId(Long villageId) {
        List<VillageImage> villageImages = villageImageRepository.findDeletedByVillageId(villageId);
        return getVillageImageDTOsByVillageId(villageImages);
    }

    public List<VillageImageDTO> getVillageImageDTOsByVillageId(List<VillageImage> villageImages) {
        return villageImages.stream().map(villageImage -> {
            VillageImageDTO villageImageDTO = villageImageToVillageImageDTO(villageImage);
            villageImageDTO.setBase64Image(imageService.getImageFromSpace(villageImage.getImageName()));
            return villageImageDTO;
        }).toList();
        /*List<VillageImageDTO> villageImagesWithBase64Images = new ArrayList<>();
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
        return villageImagesWithBase64Images;*/
    }

    public VillageImageDTO getVillageImageById(Long id) {
        Optional<VillageImage> foundVillageImage = villageImageRepository.findById(id);
        if (foundVillageImage.isPresent()) {
            if (foundVillageImage.get().getUser() != null) {
                VillageImageDTO villageImageDTO = villageImageToVillageImageDTO(foundVillageImage.get());
                villageImageDTO.setUserDTO(userService.mapUserToUserDTO(foundVillageImage.get().getUser()));
                return villageImageDTO;
            } else {
                return villageImageToVillageImageDTO(foundVillageImage.get());
            }
        }
        throw new ApiRequestException(VILLAGE_IMAGE_ID_MESSAGE + id + NOT_FOUND_MESSAGE);
    }

    public void deleteAllImageFilesByVillageId(Long villageId) {
        List<VillageImage> villageImages = villageImageRepository.findByVillageId(villageId);
        for (VillageImage villageImage : villageImages) {
            deleteImageFileById(villageImage.getId());
        }
    }


    public void deleteImageFileById(Long id) {
        VillageImageDTO villageImageDTO = getVillageImageById(id);
        if (villageImageDTO != null) {
            imageService.deleteImage(villageImageDTO.getImageName());
            deleteVillageImageById(id);
            /*String imageName = villageImageDTO.getImageName();
            if (imageName != null) {
                String imagePath = UPLOAD_DIRECTORY + imageName;
                File fileToDelete = new File(imagePath);
                if (fileExists(fileToDelete) && deleteFileWithRetries(fileToDelete)) {
                    deleteVillageImageById(id);
                }
            }*/
        }
    }

    public boolean fileExists(File file) {
        return file.exists();
    }

    public boolean deleteFileWithRetries(File file) {
        int maxRetries = 6;
        for (int i = 0; i < maxRetries; i++) {
            try {
                Path filePath = file.toPath();
                Files.delete(filePath);
                return true;
            } catch (IOException e) {
                logger.error("An error occurred while deleting the image {}", file.getAbsolutePath());
                try {
                    Thread.sleep(500);
                    System.gc();
                } catch (InterruptedException ex) {
                    logger.error("Thread interrupted while sleeping");
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }

    public void deleteVillageImageById(Long id) {
        if (villageImageRepository.existsById(id)) {
            villageImageRepository.deleteById(id);
        } else {
            throw new ApiRequestException(VILLAGE_IMAGE_ID_MESSAGE + id + NOT_FOUND_MESSAGE);
        }
    }

    public VillageImageDTO resumeImageById(Long id) {
        Optional<VillageImage> villageImage = villageImageRepository.findById(id);
        if (villageImage.isPresent()) {
            villageImage.get().setDateDeleted(null);
            return villageImageToVillageImageDTO(villageImageRepository.save(villageImage.get()));
        }
        throw new ApiRequestException(VILLAGE_IMAGE_ID_MESSAGE + id + NOT_FOUND_MESSAGE);
    }

    public boolean isImageFile(File imageFile) {
        return imageFile.getName().toLowerCase().endsWith(".jpg") || imageFile.getName().toLowerCase().endsWith(".png");
    }

    public List<File> getAllImageFilesFromDirectory() {
        System.out.println("----------------- getAllImageFilesFromDirectory ---------- " + UPLOAD_DIRECTORY + "---" + getUploadDirectoryPath());

        List<File> imageFiles = new ArrayList<>();
        File directory = new File("/opt/villagelife/villagelife-api/src/main/resources/static/village_images");
        System.out.println("---- isDirectory: " + directory.isDirectory() + "---Name: " + directory.getName() + "---AbsolutePath: " + directory.getAbsolutePath() + "---------");
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Directory does not exist or is not a directory: " + UPLOAD_DIRECTORY);
        }
        System.out.println("--------- after if ---------------");
        File[] files = directory.listFiles();
        System.out.println("----------------" + files.length + "----------------");

        try (Stream<Path> stream = Files.list(Paths.get("/opt/villagelife/villagelife-api/target/src/main/resources/static/village_images"))) {
            List<File> images = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toFile)
                    .filter(this::isImageFile)
                    .collect(toList());

            imageFiles.addAll(images);
            System.out.println("------------------------" + images.size() + "------------------------");
        } catch (IOException ioException) {
            throw new ApiRequestException("Files.list does not work");
        }

        return imageFiles;
    }

    public void uploadImages() {
        List<File> images = getAllImageFilesFromDirectory();
        LocalDateTime localDateTime = TimestampUtils.getCurrentTimestamp();
        System.out.println("--------------- Start --------------- ");
        for (File image : images) {
            String name = getVillageNameFromFileName(image.getName());
            System.out.println("village name: " + name);
            try {
                VillageDTO villageDTO = villageService.getVillageByName(name);
                createVillageImageDTO(new VillageImageDTO(null, villageDTO.getId(), image.getName(), true, localDateTime, null, null, null));
                System.out.println("villageDTO id: " + villageDTO.getId() + "\nvillageDTO name: " + villageDTO.getName());
            } catch (Exception ex) {
                logger.warn("Error while processing image with fileName {}", image.getName());
            }
        }
        System.out.println("--------------- End --------------- " + getUploadDirectoryPath());
    }

    public String getVillageNameFromFileName(String fileName) {
        fileName = fileName.replace(".png", "").replace(".jpg", "");
        String[] parts = fileName.split("-");
        if (parts.length > 1 && !parts[parts.length - 1].contains("x")) {
            StringBuilder reconstructedFileName = new StringBuilder();
            for (int i = parts[0].equalsIgnoreCase("село") ? 1 : 0; i < (parts[parts.length - 1].matches("[а-яА-Я]+") ? parts.length : parts.length - 1); i++) {
                reconstructedFileName.append(parts[i]).append("-");
            }
            String withoutHyphens = reconstructedFileName.toString().replace("-+", "");
            fileName = withoutHyphens.trim();
        }
        String withoutDigits = fileName.replaceFirst("\\d+$", "");
        return withoutDigits.replace("-", " ").trim();
    }

}
