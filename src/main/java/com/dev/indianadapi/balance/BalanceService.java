package com.dev.indianadapi.balance;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.authentication.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final UserAccountService userAccountService;

    private final BalanceMapper balanceMapper;


    public BalanceResponse getBalanceByUserAccountId(Long userAccountId) {

        log.info("Get balance by id: " + userAccountId);

        UserAccount user = userAccountService.findUserAccountById(userAccountId);

        Balance balance =  balanceRepository.findByUserAccount(user)
                .orElseThrow(() -> new RuntimeException("Balance not found"));

        return balanceMapper.balanceToResponse(balance);

    }

    public Balance createUserAccountBalance(UserAccount userAccount) {

        Balance balance = Balance.builder()
                .coins(10)
                .userAccount(userAccount)
                .build();

        return balanceRepository.save(balance);
    }

    public void addCoins(Long userAccountId, Integer amount) {

        Balance balance = balanceRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new RuntimeException("Баланс не найден"));
        balance.setCoins(balance.getCoins() + amount);

        balanceRepository.save(balance);
    }

    public void deductCoins(Long userAccountId, Integer amount) {

        Balance balance = balanceRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new RuntimeException("Баланс не найден"));

        if (balance.getCoins() < amount) {
            throw new RuntimeException("Недостаточно средств на балансе");
        }
        balance.setCoins(balance.getCoins() - amount);

        balanceRepository.save(balance);
    }

}
