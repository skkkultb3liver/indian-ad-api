package com.dev.indianadapi.authentication.dto;

public record JwtAuthResponse(
        String accessToken,
        String refreshToken
) {
}
