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

    public UserAccount saveUser(UserAccount user) {
        log.info("CustomUSerDetailsService: TRY TO SAVE USER ACCOUNT");
        return repository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }


    public UserAccount findUserByJwt(String token) {
        String jwt = token.substring(7);
        String userEmail = jwtService.extractUsername(jwt);

        return repository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя")
        );
    }

    public UserAccount findUserById(Long userId) {
        return repository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя")
        );
    }

    public UserAccount findUserByUid(String uid) {
        return repository.findByUid(uid).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя")
        );
    }

    public UserAccount findUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя")
        );
    }
}
