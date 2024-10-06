package com.dev.indianadapi.authentication.controller;

import com.dev.indianadapi.authentication.dto.JwtAuthResponse;
import com.dev.indianadapi.authentication.dto.RefreshTokenRequest;
import com.dev.indianadapi.authentication.dto.SignInRequest;
import com.dev.indianadapi.authentication.dto.SignUpRequest;
import com.dev.indianadapi.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> signUpHandler(
            @RequestPart("signUpRequest") SignUpRequest request,
            @RequestPart("image") MultipartFile image,
            @RequestPart("imageType") String imageType
    ) {
        try {

            String response = authenticationService.signUp(request, image, imageType);
            return ResponseEntity.ok(response);

        } catch (DataIntegrityViolationException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> signInHandler(
            @RequestBody SignInRequest request
    ) {
        try {

            JwtAuthResponse response = authenticationService.signIn(request);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshTokenHandler(
            @RequestBody RefreshTokenRequest request
    ) {
        try {
            JwtAuthResponse response = authenticationService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
