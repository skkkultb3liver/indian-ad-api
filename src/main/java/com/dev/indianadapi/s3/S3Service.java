package com.dev.indianadapi.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    private final String bucketUrl = "https://" + bucketName + "storage.yandexcloud.net/";

    private final S3Client s3Client;


    public String uploadImage(MultipartFile file, String imageType) {

        String folderName = switch (imageType) {
            case "avatar" -> "user_avatar";
            case "filmAdImage" -> "film_ad_image";
            default -> throw new IllegalArgumentException("Неизвестный тип изображения: " + imageType);
        };

        return uploadToS3(file, folderName);
    }

    private String uploadToS3(MultipartFile file, String folderName) {

        Set<String> SUPPORTED_CONTENT_TYPES = Set.of(
                "image/jpeg",
                "image/png",
                "image/ico",
                "image/webp",
                "image/svg"
        );

        try {

            String contentType = file.getContentType();

            assert contentType != null;

            if (!SUPPORTED_CONTENT_TYPES.contains(contentType)) {
                throw new IllegalArgumentException("Данный файл не является изображением");
            }

            String fileName = folderName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            return getFileUrl(fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteFileByUrl(String fileUrl) {

        String fileKey = extractKeyFromUrl(fileUrl);

        if (fileKey == null) return;

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

    }

    public String getFileUrl(String fileName) {

        return s3Client.utilities().getUrl(
            builder -> builder
                    .bucket(bucketName)
                    .key(fileName)
        )
        .toExternalForm();
    }

    private String extractKeyFromUrl(String fileUrl) {

        if (fileUrl.startsWith(bucketUrl)) {
            return fileUrl.substring(bucketUrl.length());
        }

        return null;
    }

}
