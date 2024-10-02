package com.dev.indianadapi.authentication.service;

import com.dev.indianadapi.authentication.dto.JwtAuthResponse;
import com.dev.indianadapi.authentication.dto.RefreshTokenRequest;
import com.dev.indianadapi.authentication.dto.SignInRequest;
import com.dev.indianadapi.authentication.dto.SignUpRequest;
import com.dev.indianadapi.authentication.entity.RefreshToken;
import com.dev.indianadapi.authentication.entity.RoleEntity;
import com.dev.indianadapi.authentication.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    private JwtAuthResponse authenticateUser(String userEmail, String userPassword) {

        try {

            log.info("[AuthenticationService] Try to authenticate");

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, userPassword)
            );

            var user = userDetailsService.findUserByEmail(userEmail);
            var jwt = jwtService.generateToken(user);
            var refreshToken = tokenService.generateToken(userEmail);

            return new JwtAuthResponse(jwt, refreshToken.getToken());

        }
        catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Неверная почта или пароль");
        }

    }

    public JwtAuthResponse signIn(SignInRequest request) {

            log.info("[AuthenticationService] Try to auth with email {}", request.userEmail());
            return authenticateUser(request.userEmail(), request.password());

    }

    public String signUp(SignUpRequest request) {

        UserAccount createdUser = UserAccount.builder()
                .email(request.userEmail())
                .uid(request.uid())
                .password(passwordEncoder.encode(request.password()))
                .role(RoleEntity.ROLE_USER)
                .build();

        log.info("AuthenticationService: try to register user");

        try {

            userDetailsService.saveUser(createdUser);
            return "Вы успешно зарегистрировались! Пожалуйста, войдите в аккаунт";
        }
        catch (Exception e) {
            throw new DataIntegrityViolationException("Пользователь с такой почтой уже существет");
        }
    }

    public JwtAuthResponse refreshToken(RefreshTokenRequest request) {

        return tokenService.findByToken(request.refreshToken())
                .map(tokenService::validateToken)
                .map(RefreshToken::getUserAccount)
                .map(
                        userAccount -> {
                            var jwt = jwtService.generateToken(userAccount);

                            return new JwtAuthResponse(jwt, request.refreshToken());
                        }
                ).orElseThrow(
                        () -> new UsernameNotFoundException("Не удалось найти вашу сессию")
                );
    }

}
