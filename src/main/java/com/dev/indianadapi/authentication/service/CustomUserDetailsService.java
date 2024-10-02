package com.dev.indianadapi.authentication.service;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.authentication.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.spi.JdbcWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя: " + username)
        );
    }

    public UserAccount findUserByJwt(String token) {
        String jwt = token.substring(7);
        String userEmail = jwtService.extractUsername(jwt);

        return repository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя")
        );
    }

}
