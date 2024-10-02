package com.dev.indianadapi.authentication.repository;

import com.dev.indianadapi.authentication.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByUid(String uid);
}
