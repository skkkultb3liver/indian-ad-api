package com.dev.indianadapi.authentication.service;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.authentication.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository repository;

    public UserAccount saveUser(UserAccount user) {
        return repository.save(user);
    }

    public UserAccount findUserAccountById(Long userId) {
        return repository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя")
        );
    }

    public UserAccount findUserAccountByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя")
        );
    }

    public UserAccount findUserAccountByUid(String uid) {
        return repository.findByUid(uid).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя")
        );
    }

}
