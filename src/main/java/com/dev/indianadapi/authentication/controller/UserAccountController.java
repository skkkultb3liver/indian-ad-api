package com.dev.indianadapi.authentication.controller;

import com.dev.indianadapi.authentication.dto.UserAccountProfileResponse;
import com.dev.indianadapi.authentication.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/{uid}")
    public ResponseEntity<UserAccountProfileResponse> getUserAccountProfileHandler(
            @PathVariable String uid,
            @RequestHeader("Authorization") String token
    ) {

        return ResponseEntity.ok(userAccountService.getUserProfile(uid, token));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserAccountProfileHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestPart("image") MultipartFile image,
            @RequestPart("newUid") String newUid
    ) {
        userAccountService.updateUserAccountProfile(jwt, image, newUid);

        return ResponseEntity.ok("Успешная смена аватара");
    }

}
