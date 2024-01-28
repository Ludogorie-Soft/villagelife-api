package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.model.VillageImage;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    @Value("${digital.ocean.bucket.name}")
    private String digitalOceanBucketName;
    private final MinioClient minioClient;

    public String uploadImage(final byte[] imageData, String randomUuid) {
        try {
            InputStream inputStream = new ByteArrayInputStream(imageData);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(digitalOceanBucketName)
                    .object(randomUuid).contentType("image/jpeg")
                    .stream(inputStream, inputStream.available(), -1)
                    .build());

            return randomUuid;
        } catch (MinioException e) {
            log.warn("Minio error: " + e.getMessage());
        } catch (IOException e) {
            log.warn("Error uploading file: " + e.getMessage());
        } catch (Exception e) {
            log.warn("An unexpected error occurred: " + e.getMessage());
        }
        return null;
    }

//    public String uploadImage(final List<byte[]> imageBytes, String randomUuid) {
//        try {
//            // Concatenate the list of byte arrays into a single byte array
//            byte[] concatenatedBytes = concatenateBytes(imageBytes);
//
//            // Convert the concatenated byte array to an InputStream
//            InputStream inputStream = new ByteArrayInputStream(concatenatedBytes);
//
//            // Upload the file to DigitalOcean Spaces
//            minioClient.putObject(PutObjectArgs.builder()
//                    .bucket(digitalOceanBucketName)
//                    .object(randomUuid)
//                    .contentType("image/jpeg")
//                    .stream(inputStream, inputStream.available(), -1)
//                    .build());
//
//            return randomUuid;
//        } catch (MinioException e) {
//            // Handle Minio-specific exceptions
//            log.warn("Minio error: " + e.getMessage());
//        } catch (IOException e) {
//            // Handle general IO exceptions
//            log.warn("Error uploading file: " + e.getMessage());
//        } catch (Exception e) {
//            // Handle other exceptions
//            log.warn("An unexpected error occurred: " + e.getMessage());
//        }
//        return null;
//    }

//    private byte[] concatenateBytes(List<byte[]> byteArrays) throws IOException {
//        int totalSize = byteArrays.stream().mapToInt(array -> array.length).sum();
//        byte[] result = new byte[totalSize];
//        int offset = 0;
//        for (byte[] byteArray : byteArrays) {
//            System.arraycopy(byteArray, 0, result, offset, byteArray.length);
//            offset += byteArray.length;
//        }
//
//        return result;
//    }

    public String getImageFromSpace(String objectKey) {
        try {
            try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(digitalOceanBucketName).object(objectKey).build())) {
                byte[] imageBytes = IOUtils.toByteArray(inputStream);
                return Base64.encodeBase64String(imageBytes);
            }
        } catch (MinioException e) {
            log.warn("Minio error: " + e.getMessage());
        } catch (Exception e) {
            log.warn("An unexpected error occurred: " + e.getMessage());
        }
        return null;
    }

    public boolean deleteImage(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(digitalOceanBucketName).object(objectName).build());
            return true;
        } catch (MinioException e) {
            log.warn("Minio error while deleting image: " + e.getMessage());
        } catch (Exception e) {
            log.warn("An unexpected error occurred: " + e.getMessage());
        }
        return false;
    }

//    public List<VillageDTO> getVillagesWithImages(List<VillageDTO> villages) {
//        System.out.println("1111111" + villages.get(villages.size() - 1).getImages());
//        return villages.stream()
//                .map(villageDTO -> {
//                    villageDTO.setImages(villageDTO.getImages().stream()
//                            .map(this::getImageFromSpace)
//                            .toList());
//                    System.out.println("2222222" + villages.get(villages.size() - 1).getImages());
//                    System.out.println("33333" + villageDTO.getImages());
//                    return villageDTO;
//                }).toList();
//    }
//public List<VillageImage> getVillagesWithImages(List<VillageImage> villages) {
//    System.out.println("1111111" + villages.get(villages.size() - 1).getImageName());
//    return villages.stream()
//            .map(villageImage -> {
//                villageImage.setImageName(villageImage.getImages().stream()
//                        .map(this::getImageFromSpace)
//                        .toList());
//                System.out.println("2222222" + villages.get(villages.size() - 1).getImages());
//                System.out.println("33333" + villageImage.getImages());
//                return villageImage;
//            }).toList();
//}

}
