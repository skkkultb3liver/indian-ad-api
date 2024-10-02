package com.dev.indianadapi.authentication.service;

import com.dev.indianadapi.authentication.entity.RefreshToken;
import com.dev.indianadapi.authentication.repository.RefreshTokenRepository;
import com.dev.indianadapi.authentication.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository tokenRepository;
    private final UserAccountRepository userRepository;

    @Value("${security.refresh.lifetime}")
    private Long REFRESH_TOKEN_LIFETIME;

    public RefreshToken generateToken(String userEmail) {

        RefreshToken refreshToken = RefreshToken.builder()
                .userAccount(userRepository.findByEmail(userEmail).get())
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plus(Duration.ofMillis(REFRESH_TOKEN_LIFETIME)))
                .build();

        return tokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {

        return tokenRepository.findByToken(token);
    }

    public RefreshToken validateToken(RefreshToken token) {

        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            deleteRefreshToken(token);

            throw new RuntimeException("Ваша сессия окончена. Пожалуйста, авторизуйтесь повторно " + token.getToken());
        }

        return token;
    }

    private void deleteRefreshToken(RefreshToken token) {
        tokenRepository.delete(token);
    }

}
