package com.example.ludogorieSoft.village.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;

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

    public List<String> getAllImageNamesFromSpace() {
        List<String> imageNames = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(digitalOceanBucketName).recursive(true).build());
            for (Result<Item> result : results) {
                System.out.println("1111111111111111111");
                Item item = result.get();
                System.out.println("222222222222222222222");
                String objectKey = item.objectName();
                System.out.println("3333333333333333333333");
                imageNames.add(objectKey);
            }
        } catch (MinioException e) {
            log.warn("Minio error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.warn("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return imageNames;
    }

    /*public void test() {
        System.out.println("---- in test ----");
        ListObjectsArgs lArgs = ListObjectsArgs.builder().bucket(digitalOceanBucketName).build();
        System.out.println("1");
        Iterable<Result<Item>> resp = minioClient.listObjects(lArgs);
        System.out.println("2");
        Iterator<Result<Item>> it = resp.iterator();
        System.out.println("3");
        while (it.hasNext()){
            try {
                System.out.println("4");
                Item i = it.next().get();
                System.out.println("+++++++" + i.objectName() + "++++++++");
            } catch (MinioException e) {
                log.warn("Minio error: " + e.getMessage());
            } catch (Exception e) {
                log.warn("An unexpected error occurred: " + e.getMessage());
            }
        }
    }*/

}