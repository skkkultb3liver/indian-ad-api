package com.dev.indianadapi.authentication.dto;

public record SignUpRequest(
        String userEmail,
        String uid,
        String password
) {
}
