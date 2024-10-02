package com.dev.indianadapi.authentication.dto;

public record SignInRequest(
        String userEmail,
        String password
) {
}
