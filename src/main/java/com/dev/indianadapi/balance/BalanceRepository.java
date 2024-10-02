package com.dev.indianadapi.balance;

import com.dev.indianadapi.authentication.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByUserAccount(UserAccount userAccount);
    Optional<Balance> findByUserAccountId(Long userAccountId);
}
