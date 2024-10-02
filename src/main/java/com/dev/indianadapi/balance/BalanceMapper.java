package com.dev.indianadapi.balance;

import org.springframework.stereotype.Service;

@Service
public class BalanceMapper {

    public BalanceResponse balanceToResponse(Balance balance) {

        if (balance == null) {
            return null;
        }

        return new BalanceResponse(
                balance.getId(),
                balance.getCoins(),
                balance.getUserAccount().getId()
        );
    }

}
