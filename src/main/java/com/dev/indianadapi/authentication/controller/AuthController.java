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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> signUpHandler(
            @RequestBody SignUpRequest request
    ) {
        try {

            String response = service.signUp(request);
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

            JwtAuthResponse response = service.signIn(request);
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
            JwtAuthResponse response = service.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
