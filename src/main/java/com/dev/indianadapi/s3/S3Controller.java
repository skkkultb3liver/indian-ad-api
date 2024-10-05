package com.dev.indianadapi.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service service;

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadUserAvatarHandler(
            @RequestParam("file") MultipartFile file
    ) {

        String fileUrl = service.uploadImage(file, "avatar");

        return ResponseEntity.ok(fileUrl);
    }

    @PostMapping("/upload-film-ad-image")
    public ResponseEntity<String> uploadFilmAdImageHandler(
            @RequestParam("file") MultipartFile file
    ) {

        String fileUrl = service.uploadImage(file, "filmAdImage");

        return ResponseEntity.ok(fileUrl);
    }


}
