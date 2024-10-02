package com.dev.indianadapi.balance;

public record BalanceResponse(
        Long id,
        Integer coins,
        Long userId
) {
}
