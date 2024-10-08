package com.dev.indianadapi.balance;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.authentication.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final CustomUserDetailsService userDetailsService;

    private final BalanceMapper balanceMapper;


    public Balance getBalanceByUserAccountId(Long userAccountId) {

        log.info("Get balance by id: {}", userAccountId);

        UserAccount user = userDetailsService.findUserAccountById(userAccountId);

        Balance balance =  balanceRepository.findByUserAccount(user)
                .orElseThrow(() -> new RuntimeException("Balance not found"));

        return balance;

    }


    public Balance createUserAccountBalance(UserAccount userAccount) {

        Balance balance = Balance.builder()
                .balance(10)
                .userAccount(userAccount)
                .build();

        return balanceRepository.save(balance);
    }

    public void add(Long userAccountId, Integer amount) {

        Balance balance = balanceRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new RuntimeException("Баланс не найден"));
        balance.setBalance(balance.getBalance() + amount);

        balanceRepository.save(balance);
    }

    public void deduct(Long userAccountId, Integer amount) {

        Balance balance = balanceRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new RuntimeException("Баланс не найден"));

        if (balance.getBalance() < amount) {
            throw new RuntimeException("Недостаточно средств на балансе");
        }
        balance.setBalance(balance.getBalance() - amount);

        balanceRepository.save(balance);
    }

    public void saveBalance(Balance balance) {
        balanceRepository.save(balance);
    }

}
