package com.example.ludogorieSoft.village.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private static final String MINIO_ERROR = "Minio error: ";
    private static final String UNEXPECTED_ERROR = "An unexpected error occurred: ";

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
            log.warn(MINIO_ERROR + e.getMessage());
        } catch (IOException e) {
            log.warn("Error uploading file: " + e.getMessage());
        } catch (Exception e) {
            log.warn(UNEXPECTED_ERROR + e.getMessage());
        }
        return null;
    }

    public String getImageFromSpace(String objectKey) {
        try {
            try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(digitalOceanBucketName).object(objectKey).build())) {
                byte[] imageBytes = IOUtils.toByteArray(inputStream);
                return Base64.encodeBase64String(imageBytes);
            }
        } catch (MinioException e) {
            log.warn(MINIO_ERROR + e.getMessage());
        } catch (Exception e) {
            log.warn(UNEXPECTED_ERROR + e.getMessage());
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
            log.warn(UNEXPECTED_ERROR + e.getMessage());
        }
        return false;
    }

    public List<String> getAllImageNamesFromSpace() {
        List<String> imageNames = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(digitalOceanBucketName).recursive(true).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                String objectKey = item.objectName();
                imageNames.add(objectKey);
            }
        } catch (MinioException e) {
            logger.error(MINIO_ERROR + e.getMessage(), e);
        } catch (Exception e) {
            logger.error(UNEXPECTED_ERROR + e.getMessage(), e);
        }
        return imageNames;
    }
}